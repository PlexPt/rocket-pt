package com.rocketpt.server.common.base;

import com.rocketpt.server.common.CommonResultStatus;
import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.exception.RocketPTException;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPageParam extends PageParam {

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    protected String prop;

    /**
     * 排序规则
     */
    @Schema(description = "排序规则")
    protected String sort;

    public void validOrder(List<String> orderKey) throws RocketPTException {
        prop = StringUtils.isBlank(prop) ? null : StrUtil.toUnderlineCase(prop);
        sort = StringUtils.isBlank(sort) ? Constants.Order.DEFAULT_ORDER_TYPE : sort;

        if (Arrays.asList(Constants.Order.ORDER_TYPE).indexOf(sort) < 0) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "排序方式錯誤");
        }

        if (StringUtils.isNotBlank(prop) && Arrays.asList(orderKey).indexOf(prop) < 0) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "排序欄位錯誤");
        }
    }

    public void validOrder() throws RocketPTException {
        List<String> orderKey = getOrderKey();
        prop = StringUtils.isBlank(prop) ? null : StrUtil.toUnderlineCase(prop);
        sort = StringUtils.isBlank(sort) ? Constants.Order.DEFAULT_ORDER_TYPE : sort;

        if (!ArrayUtil.contains(Constants.Order.ORDER_TYPE, sort)) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "排序方式錯誤");
        }

        if (StringUtils.isNotBlank(prop) && !orderKey.contains(prop)) {
            throw new RocketPTException(CommonResultStatus.PARAM_ERROR, "排序欄位錯誤");
        }
    }

    /**
     * @return 反射获取字段列表
     */
    public List<String> getOrderKey() {
        List<String> list = new ArrayList<>();

        Field[] fields = getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();

                list.add(StrUtil.toUnderlineCase(name));
            }
        }
        return list;
    }
    /**
     * @return 反射获取字段列表
     */
    public List<String> getOrderKey(Class clazz) {
        List<String> list = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();

                list.add(StrUtil.toUnderlineCase(name));
            }
        }
        return list;
    }

}
