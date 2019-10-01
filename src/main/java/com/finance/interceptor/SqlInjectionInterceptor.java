package com.finance.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        })
        , @Signature(type = Executor.class, method = "query", args = {
        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class
})
        , @Signature(type = Executor.class, method = "update", args = {
        MappedStatement.class, Object.class})
})
public class SqlInjectionInterceptor implements Interceptor {

    private static String escapeInputString(String string) {
        String regex = "(?i)update|insert|select|delete|count\\(\\*\\)|count\\(1\\)|truncate|create|union|exec|execute|declare|grant|--|drop";
        string = string.replaceAll(regex, "　");
        return string;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object[] args = invocation.getArgs();
            MappedStatement stmt = (MappedStatement) args[0];
            Object param = args[1];
            if (stmt == null) {
                return invocation.proceed();
            }
            if (stmt.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                if (param != null) {
                    GsonBuilder gsonBuilder =
                            new GsonBuilder()
                                    .serializeSpecialFloatingPointValues()
                                    .serializeNulls()
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .registerTypeAdapter(String.class,
                                            new StringSerializer());
                    Gson gson = gsonBuilder.create();
                    if (param instanceof Map) {
                        Map map = (Map) param;
                        for (Object object : map.entrySet()) {
                            Entry entry = (Entry) object;
                            Object value = entry.getValue();
                            if (value != null) {
                                String jsonString = gson.toJson(value);
                                entry.setValue(gson.fromJson(jsonString, value.getClass()));
                            }
                        }
                    } else {
                        String jsonString = gson.toJson(param);
                        param = gson.fromJson(jsonString, param.getClass());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    @Override
    public void setProperties(Properties properties) {

    }

    private static class StringSerializer implements JsonSerializer<String> {
        @Override
        public JsonElement serialize(String src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(escapeInputString(src));
        }
    }

//    public static void main(String[] temp) {
//        GsonBuilder gsonBuilder =
//                new GsonBuilder()
//                        .serializeSpecialFloatingPointValues()
//                        .serializeNulls()
//                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                        .registerTypeAdapter(String.class,
//                                new StringSerializer());
//        Gson gson = gsonBuilder.create();
//        List<FundDO> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(new FundDO());
//        }
//        List<FundDO> sublist = list.subList(0, 5);
//        Object object = sublist;
//        Type type = ParameterizedTypeImpl.make(object.getClass(), new Type[]{}, null);
//        String json = gson.toJson(object);
//        Object result = gson.fromJson(json, type);
//        System.out.println(result);
//    }

}


