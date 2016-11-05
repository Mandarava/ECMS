package com.finance.util.myutil;

import com.finance.model.dto.Page;
import com.finance.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


public final class PageUtils {

    @Autowired
    private static HttpServletRequest request;

    public static Page initPage() {
        // 当前页
        String currentPage = request.getParameter("currentPage");
        String limit = request.getParameter("limit");
        // 排序语句
        String sortKey = request.getParameter("sortKey");

        Page page = new Page();
        Pattern pattern = Pattern.compile("^[0-9]*$");
        // 判断当前页是否合法
        if (currentPage == null || !pattern.matcher(currentPage).matches()) {
            page.setPageNo(1);
        } else {
            page.setPageNo(Integer.valueOf(currentPage));
        }

        // 判断limit是否合法
        if (limit == null || !pattern.matcher(limit).matches()) {
            page.setLimit(99999);
        } else {
            page.setLimit(Integer.valueOf(limit));
        }

        // 设置排序语句
        if (!CommonUtils.isElementBlank(sortKey)) {
            page.setSortKey(sortKey);
        } else {
            page.setSortKey(null);
        }

        page.setOffset((page.getPageNo() - 1) * page.getLimit());

        return page;

    }

}
