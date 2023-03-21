package com.example.myprojectdemo.mproot.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 日期范围查询
 *
 * @author Joon
 * @date 2023-03-17 15:33
 */
@Data
@Schema(title = "日期范围查询")
public class DateRange {
    @Schema(title = "开始时间")
    private Object startTime;
    @Schema(title = "结束时间")
    private Object endTime;
}
