package com.rocketpt.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocketpt.server.dto.entity.SuggestEntity;
import com.rocketpt.server.dto.vo.SuggestVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yzr
 */
@Mapper
public interface SuggestDao extends BaseMapper<SuggestEntity> {


    @Select("SELECT keyword AS suggest, COUNT(*) AS count " +
            "FROM t_suggest WHERE keyword LIKE #{searchStr} " +
            "GROUP BY keyword " +
            "ORDER BY count DESC, keyword DESC LIMIT 10")
    List<SuggestVo> getSuggestions(@Param("searchStr") String searchStr);

}
