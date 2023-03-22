package com.joon.demo.mproot.common.web.param;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

/**
 * 可排序查询参数对象
 *
 * @author Joon
 * @date 2023-03-17 15:32
 */
@Data
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Schema(title = "可排序查询参数对象")
public abstract class OrderQueryParam extends QueryParam {
    private static final long serialVersionUID = 57714391204790143L;

    @Schema(title = "排序")
    private List<OrderItem> orders;

    public void defaultOrder(OrderItem orderItem) {
        this.defaultOrders(Arrays.asList(orderItem));
    }

    public void defaultOrders(List<OrderItem> orderItems) {
        if (CollectionUtil.isEmpty(orderItems)) {
            return;
        }
        this.orders = orderItems;
    }

}
