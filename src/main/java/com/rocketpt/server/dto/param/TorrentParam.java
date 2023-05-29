package com.rocketpt.server.dto.param;

import com.rocketpt.server.common.base.OrderPageParam;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 种子查询
 */
@Data
@Schema(description = "种子查询")
public class TorrentParam extends OrderPageParam {


    @Schema(description = "关键字")
    String keyword;


    @Schema(description = "分类")
    Integer category;

    @Schema(description = "状态")
    Integer status;

    @Schema(description = "审核状态")
    Integer reviewStatus;

    //促销种子？
    @Schema(description = "促销种子？")
    String free;


    private Set<String> likeExpressions;

    public void buildLike() {
        likeExpressions = new LinkedHashSet<>();
        if (StringUtils.isEmpty(keyword)) {
            return;
        }

        keyword = keyword.replace(".", " ");
        String[] searchstrExploded = keyword.split(" ");


        for (int i = 0; i < searchstrExploded.length && i < 10; i++) {
            String searchstrElement = searchstrExploded[i].trim();
            likeExpressions.add(searchstrElement);
        }


    }
}
