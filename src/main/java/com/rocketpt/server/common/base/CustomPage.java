package com.rocketpt.server.common.base;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单分页模型
 *
 * @author hubin
 * @since 2018-06-09
 */
@Data
public class CustomPage {

    /**
     * 总数
     */
    protected long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;

    /**
     * 排序字段信息
     */
    @Setter
    protected List<OrderItem> orders = new ArrayList<>();

    /**
     * 自动优化 COUNT SQL
     */
    protected boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    protected boolean searchCount = true;
    @Setter
    protected boolean optimizeJoinOfCountSql = true;
    /**
     * 单页分页条数限制
     */
    @Setter
    protected Long maxLimit;
    /**
     * countId
     */
    @Setter
    protected String countId;

    public CustomPage() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public CustomPage(long current, long size) {
        this(current, size, 0);
    }

    public CustomPage(long current, long size, long total) {
        this(current, size, total, true);
    }

    public CustomPage(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    public CustomPage(long current, long size, long total, boolean searchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }
}
