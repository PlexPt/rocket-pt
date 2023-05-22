package com.rocketpt.server.common.base;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PageParam {
    @NotNull(message = "page参数不能为空")
    @Min(value = 1L, message = "page参数必须是数字或数值小于限制")
    protected Integer page;

    @NotNull(message = "参数不能为空")
    @Min(value = 1L, message = "参数必须是数字或数值小于限制")
    @Max(value = 200L, message = "参数必须是数字或数值大于限制")
    protected Integer size;


}
