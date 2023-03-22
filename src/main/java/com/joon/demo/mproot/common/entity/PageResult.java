package com.joon.demo.mproot.common.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础分页结果
 *
 * @author Joon
 * @date 2023-03-17 17:34
 */
@Schema(title = "分页结果响应实体")
public class PageResult<T> implements IPage<T> {

    /** 查询数据列表 */
    @Schema(title = "查询结果集合")
    protected List<T> records = Collections.emptyList();
    /** 总数 */
    @Schema(title = "总条数")
    protected long total = 0;
    /** 每页显示条数，默认 10 */
    @Schema(title = "当前页查询条数")
    protected long size = 10;
    /** 当前页 */
    @Schema(title = "当前页")
    protected long current = 1;

    /** 排序条件 */
    protected List<OrderItem> orders = new LinkedList<>();

    public PageResult() {
    }

    public PageResult(long current, long size) {
        this(current, size, 0);
    }

    public PageResult(long current, long size, long total) {
        this.setCurrent(current);
        this.setSize(size);
        this.setTotal(total);
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    /**
     * 兼容Collections.emptyList()
     *
     * @return
     */
    @JsonIgnore
    public List<T> getModifiableRecords() {
        return this.records == Collections.emptyList() ? new ArrayList<>(0) : this.records;
    }

    @Override
    public PageResult<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public PageResult<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public PageResult<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public PageResult<T> setCurrent(long current) {
        this.current = current;
        return this;
    }


    /**
     * 添加新的排序条件
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public PageResult<T> addOrder(OrderItem items) {
        orders.add(items);
        return this;
    }

    @Override
    @JsonIgnore
    public List<OrderItem> orders() {
        return this.orders;
    }

    @JsonIgnore
    public boolean hashRecords() {
        return !CollectionUtils.isEmpty(this.getRecords());
    }
}
