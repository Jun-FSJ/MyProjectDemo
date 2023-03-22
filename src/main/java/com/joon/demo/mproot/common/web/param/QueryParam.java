package com.joon.demo.mproot.common.web.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询参数对象
 *
 * @author Joon
 * @date 2023-03-17 15:32
 */
@Deprecated
@Data
@Schema(title = "查询参数对象")
public abstract class QueryParam implements Serializable{
    private static final long serialVersionUID = -3263921252635611410L;

    @Schema(title = "页码,默认为1")
	private Integer page =1;
	@Schema(title = "页大小,默认为10")
	private Integer limit = 10;
    @Schema(title = "搜索字符串")
    private String keyword;

    @Schema(title = "当前第几页")
    public void setCurrent(Integer current) {
	    if (current == null || current <= 0){
	        this.page = 1;
        }else{
            this.page = current;
        }
    }

    public void setSize(Integer size) {
	    if (size == null || size <= 0){
	        this.limit = 10;
        }else{
            this.limit = size;
        }
    }

}
