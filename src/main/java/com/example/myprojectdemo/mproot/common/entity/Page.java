package com.example.myprojectdemo.mproot.common.entity;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * 基础分页类
 *
 * @author Joon
 * @date 2023-03-17 14:41
 */
@Getter
@Schema(title = "分页")
public class Page {
    /** 当前页 */
    @Schema(title = "当前页")
    private long page = 1;
    /** 查询数量 */
    @Schema(title = "查询数量")
    private long size = 10;
    /** 升序排列 */
    @Setter
    @Schema(title = "升序排列字段，可以用,传递多个")
    private String ascOrder;
    /** 降序排列 */
    @Setter
    @Schema(title = "降序排列字段，可以用,传递多个")
    private String descOrder;

    /**
     * 兼容后端接口
     *
     * @author Joon
     * @date 2022-05-21 18:10
     */
    @Deprecated
    public void setCurrent(long current) {
        this.setPage(current);
    }

    /**
     * 兼容管理后台
     *
     * @author Joon
     * @date 2022-05-21 18:10
     */
    @Deprecated
    public void setPageNo(long current) {
        this.setPage(current);
    }

    /**
     * 兼容前端接口
     *
     * @author Joon
     * @date 2022-05-21 18:10
     */
    @Deprecated
    public void setLimit(long limit) {
        this.setSize(limit);
    }

    /**
     * 分页兼容
     *
     * @param pageSize 分页参数
     */
    @Deprecated
    public void setPageSize(long pageSize) {
        this.setSize(pageSize);
    }

    /**
     * 当前页，0表示第一页
     *
     * @param page 当前页
     */
    public void setPage(long page) {
        if (page < 1) {
            throw new IllegalArgumentException("当前页必须不能小于1");
        }
        this.page = page;
    }

    public void setSize(long size) {
        if (size < 1) {
            throw new IllegalArgumentException("查询数量必须大于0");
        }
        this.size = size;
    }

    public <T> PageResult<T> page() {
        PageResult<T> page = new PageResult<>(this.page, size);
        this.initOrderInfo(page, ascOrder, true).initOrderInfo(page, descOrder, false);
        return page;
    }

    /**
     * 初始化排序信息
     *
     * @param page  分页实体
     * @param order 排序字段
     * @param asc   是否升序
     */
    private Page initOrderInfo(PageResult<?> page, String order, boolean asc) {
        if (StringUtils.isNotBlank(order)) {
            Arrays.stream(order.split(","))
                    //驼峰转下划线
                    .map(StringUtils::camelToUnderline)
                    //组建排序信息
                    .map(v -> new OrderItem(v, asc))
                    //追加排序信息
                    .forEach(page::addOrder);
        }
        return this;
    }

}
