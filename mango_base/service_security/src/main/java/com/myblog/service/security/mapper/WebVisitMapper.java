package com.myblog.service.security.mapper;

import com.myblog.service.security.entity.WebVisit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Web访问记录表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-15
 */
public interface WebVisitMapper extends BaseMapper<WebVisit> {

    @MapKey("behavior")
    List<Map<String, Object>> getWebVisitGroupByBehavior(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @MapKey("date")
    List<Map<String, Object>> getVisitByWeek(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<String> groupByIp(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
