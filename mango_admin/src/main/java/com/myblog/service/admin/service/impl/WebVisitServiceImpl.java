package com.myblog.service.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.service.WebVisitService;
import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.WebVisit;
import com.myblog.service.security.entity.dto.WebVisitDto;
import com.myblog.service.security.mapper.WebVisitMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * Web访问记录表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-15
 */
@Slf4j
@Service
public class WebVisitServiceImpl extends ServiceImpl<WebVisitMapper, WebVisit> implements WebVisitService {

    @Value("${dashboard.webvisit.radarMaxValue:100}")
    private Integer radarMaxValue;

    @Override
    public Response getWebVisitByPage(WebVisitDto webVisitDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<WebVisit> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(webVisitDto.getPage())) page = webVisitDto.getPage();
        if (Objects.nonNull(webVisitDto.getSize())) size = webVisitDto.getSize();

        if (!CollectionUtils.isEmpty(webVisitDto.getCreateTimes()) && Objects.equals(2, webVisitDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(webVisitDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(webVisitDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        if (StringUtils.isNotBlank(webVisitDto.getBehavior())) {
            queryWrapper.like(DbConstants.WebVisit.BEHAVIOR, webVisitDto.getBehavior());
        }
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        Page<WebVisit> webVisitPage = new Page<>(page, size);
        baseMapper.selectPage(webVisitPage, queryWrapper);
        List<WebVisitDto> tagDtos = this.toDtoList(webVisitPage.getRecords(), WebVisitDto.class);
        response.data(Constants.ReplyField.DATA, tagDtos);
        response.data(Constants.ReplyField.TOTAL, webVisitPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    @Override
    public Response delWebVisit(Set<String> ids) {
        for (String id : ids) {
            if (baseMapper.deleteById(id) < 1) {
                log.error("delWebVisit failed by unknown error, webVisitId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        return Response.ok();
    }

    @Override
    public int getWebVisitCount() {
        String startTime = ThreadSafeDateFormat.getTodayStartTime();
        String endTime = ThreadSafeDateFormat.getTodayEndTime();
        QueryWrapper<WebVisit> queryWrapper = new QueryWrapper<>();
        queryWrapper.between(DbConstants.WebVisit.REQUEST_TIME, startTime, endTime);
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public Response getWebVisitGroupByBehavior() throws Exception {
        Response response = Response.ok();
        // 当天结束日期
        String endTime = ThreadSafeDateFormat.getTodayEndTime();
        // 获取当月第一天的日期
        String startTime = ThreadSafeDateFormat.getFirstDayOfMonth();
        // 获取到当月第一天到当天的日期
        List<String> betweenDates = ThreadSafeDateFormat.getDayBetweenDates(startTime, endTime);
        List<Map<String, Object>> behaviorList = baseMapper.getWebVisitGroupByBehavior(startTime, endTime);

        // key:日期, value: [{"date":20220417, "count":2, "behavior":"点击博客"}, {"date":20220417, "count":1, "behavior":"点击分类页面"}]
        Map<String, List<Map<String, Object>>> behaviorMaps = new HashMap<>();
        for (Map<String, Object> behaviorMap : behaviorList) {
            List<Map<String, Object>> dates = behaviorMaps.get(behaviorMap.get("date").toString());
            if (Objects.isNull(dates)) {
                List<Map<String, Object>> list = new ArrayList<>();
                list.add(behaviorMap);
                behaviorMaps.put(behaviorMap.get("date").toString(), list);
            } else {
                dates.add(behaviorMap);
            }
        }

        // 获取所有页面行为
        List<BehaviorEnum> menuBehaviors = BehaviorEnum.getMenuBehaviors();

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (String date : betweenDates) {
            List<Integer> behaviorCounts = new ArrayList<>();
            // 根据日期获取当天所有的页面行为访问记录
            List<Map<String, Object>> countList = behaviorMaps.get(date);
            // 根据页面行为名称，获取对应的访问次数
            for (BehaviorEnum menuBehavior : menuBehaviors) {
                if (CollectionUtils.isEmpty(countList)) {
                    behaviorCounts.add(0);
                    continue;
                }
                Map<String, Object> countMap = countList.stream()
                        .filter(map -> Objects.equals(map.get("behavior"), menuBehavior.getBehavior()))
                        .findAny().orElse(null);
                if (CollectionUtils.isEmpty(countMap)) {
                    behaviorCounts.add(0);
                    continue;
                }
                behaviorCounts.add(Integer.valueOf(countMap.get("count").toString()));

            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("name", date);
            resultMap.put("value", behaviorCounts);
            resultList.add(resultMap);
        }
        List<Map<String, Object>> menuBehaviorList = new ArrayList<>();
        for (BehaviorEnum menuBehavior : menuBehaviors) {
            Map<String, Object> menuBehaviorMap = new HashMap<>();
            menuBehaviorMap.put("text", menuBehavior.getContent());
            menuBehaviorMap.put("max", radarMaxValue);
            menuBehaviorList.add(menuBehaviorMap);
        }
        response.data(Constants.ReplyField.RADAR_INDICATOR, menuBehaviorList);
        // [{value:[100,200,300,400,500], name:"2022-04-17"}, {value:[300,100,300,400,200], name:"2022-04-18"}]
        response.data(Constants.ReplyField.RADAR_CHART_DATA, resultList);
        return response;
    }

    /**
     * 获取最近一周用户访问量
     *
     * @return
     */
    @Override
    public Response getVisitByWeek() throws Exception {
        Response response = Response.ok();
        // 当天结束日期
        String endTime = ThreadSafeDateFormat.getTodayEndTime();

        // 获取前一周的日期
        String startTime = ThreadSafeDateFormat.getDate(endTime, -6);

        List<Map<String, Object>> visitList = baseMapper.getVisitByWeek(startTime, endTime);
        Map<String, Object> visitMap = new HashMap<>();

        for (Map<String, Object> item : visitList) {
            visitMap.put(item.get("date").toString(), item.get("count"));
        }
        List<Integer> webVisitList = new ArrayList<>();
        List<String> betweenDates = ThreadSafeDateFormat.getDayBetweenDates(startTime, endTime);

        for (String day : betweenDates) {
            if (Objects.nonNull(visitMap.get(day))) {
                webVisitList.add(Integer.parseInt(visitMap.get(day).toString()));
                continue;
            }
            webVisitList.add(0);
        }
        response.data(Constants.ReplyField.DATE, betweenDates)
                .data(Constants.ReplyField.VISIT_LIST, webVisitList);
        return response;
    }
}
