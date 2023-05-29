package com.rocketpt.server.common.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageUtil {


    public static int DEFAULT_PAGE_SIZE = 20;

    /**
     * 开始分页
     *
     * @param param
     */
    public static void startPage(OrderPageParam param) {
        Integer page = param.getPage();
        if (page == null) {
            param.setPage(1);
            param.setSize(DEFAULT_PAGE_SIZE);
        }

        PageHelper.startPage(param.getPage(), param.getSize());

    }

    /**
     * 开始分页
     *
     * @param param
     */
    public static void startPage(PageParam param) {
        Integer page = param.getPage();
        if (page == null) {
            param.setPage(1);
            param.setSize(DEFAULT_PAGE_SIZE);
        }

        PageHelper.startPage(param.getPage(), param.getSize());
    }

    /**
     * 分页结果
     *
     * @param list
     */
    public static ResPage getPage(List list) {
        PageInfo pageInfo = new PageInfo(list);
        return new ResPage(pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getSize());

    }
}
