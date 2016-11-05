package com.finance.interceptor;

import com.finance.model.dto.BaseDTO;
import com.finance.model.dto.Page;
import com.finance.util.CommonUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;


/**
 * 分页拦截器
 *
 * @author zt c.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PageInterceptor implements Interceptor {

    private String pageInterceptor;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject
                .getValue("delegate.mappedStatement");

        // 配置文件中SQL语句的ID
        String id = mappedStatement.getId();

        if (id.matches(".+ByPage$")) {
            BoundSql boundSql = statementHandler.getBoundSql();
            // 原始的SQL语句
            String sql = boundSql.getSql();
            // 查询总条数的SQL语句
            String countSql = "select count(1) from (" + sql + ")a";
            Connection connection = (Connection) invocation.getArgs()[0];
            PreparedStatement countStatement = connection.prepareStatement(countSql);
            ParameterHandler parameterHandler = (ParameterHandler) metaObject
                    .getValue("delegate.parameterHandler");
            parameterHandler.setParameters(countStatement);
            ResultSet rs = countStatement.executeQuery();

            // Map<?, ?> parameter = (Map<?, ?>) boundSql.getParameterObject();
            // Page page = (Page) parameter.get("page");

            //TODO   过渡用
            Page page = null;
            try {
                Map<?, ?> parameter = (Map<?, ?>) boundSql.getParameterObject();
                page = (Page) parameter.get("page");
            } catch (Exception e) {
                BaseDTO parameter = (BaseDTO) boundSql.getParameterObject();
                page = parameter.getPage();
            }

            if (rs.next()) {
                page.setTotal(rs.getInt(1));
            }

            // 改造排序字段
            sql = addOrderKey(sql, page);

            // 改造后带分页查询的SQL语句
            String pageSql = sql + " LIMIT " + page.getLimit() + " OFFSET " + page.getOffset();
            metaObject.setValue("delegate.boundSql.sql", pageSql);
        }
        return invocation.proceed();

    }

    /**
     * 如果orderKey不为空 则替换原有的排序语句
     *
     * @param sql
     * @return
     */
    private String addOrderKey(String sql, Page page) {
        if (!CommonUtils.isElementBlank(page.getSortKey())) {
            // 替换order by内容
            sql = sql.replaceAll("(?i)ORDER BY[\\s\\S]*", "").concat(" ORDER BY ")
                    .concat(page.getSortKey());
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.pageInterceptor = properties.getProperty("pageInterceptor");

    }

}
