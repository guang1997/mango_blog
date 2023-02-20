package com.myblog.service.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.entity.Comment;
import com.myblog.service.admin.entity.User;
import com.myblog.service.admin.service.BlogService;
import com.myblog.service.admin.service.CommentService;
import com.myblog.service.admin.service.UserService;
import com.myblog.service.admin.service.WebVisitService;
import com.myblog.service.base.common.Constants.CommentStatus;
import com.myblog.service.base.common.Constants.IsDeleted;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.DbConstants.Base;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.entity.WebVisit;
import com.myblog.service.security.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-10
 */
@CrossOrigin
@RestController
@RequestMapping("/dashboard")
@Api(value = "首页相关接口", tags = {"首页相关接口"})
public class DashboardController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private WebVisitService webVisitService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @LogByMethod("/admin/dashboard/init")
    @ApiOperation(value = "首页初始化数据", notes = "首页初始化数据", response = Response.class)
    @GetMapping("/init")
    public ResultModel<Map<String, Integer>> init() throws Exception {
        int commentCount = commentService.count(new QueryWrapper<Comment>().eq(DbConstants.Comment.STATUS, CommentStatus.REVIEWED));
        int blogCount = blogService.count(new QueryWrapper<Blog>().eq(Base.IS_DELETED, IsDeleted.NO));
        int userCount = userService.count(new QueryWrapper<User>().eq(Base.IS_DELETED, IsDeleted.NO));

        String startTime = ThreadSafeDateFormat.getTodayStartTime();
        String endTime = ThreadSafeDateFormat.getTodayEndTime();
        int webVisitCount = webVisitService.count(new QueryWrapper<WebVisit>().between(DbConstants.WebVisit.REQUEST_TIME, startTime, endTime));


        Map<String, Integer> resultMap = new HashMap<>(4);
        resultMap.put(Constants.ReplyField.COMMENT_COUNT, commentCount);
        resultMap.put(Constants.ReplyField.BLOG_COUNT, blogCount);
        resultMap.put(Constants.ReplyField.VISIT_COUNT, webVisitCount);
        resultMap.put(Constants.ReplyField.USER_COUNT, userCount);
        return ResultModel.ok(resultMap);
    }

    @LogByMethod("/admin/dashboard/getVisitByWeek")
    @ApiOperation(value = "获取最近一周用户访问量", notes = "获取最近一周用户访问量", response = Response.class)
    @GetMapping("/getVisitByWeek")
    public ResultModel<Map<String, Object>> getVisitByWeek() throws Exception {
        return ResultModel.ok(webVisitService.getVisitByWeek());
    }

    @LogByMethod("/admin/dashboard/getBlogCountByTag")
    @ApiOperation(value = "获取每个标签下文章数目", notes = "获取每个标签下文章数目", response = Response.class)
    @GetMapping("/getBlogCountByTag")
    public ResultModel<List<Map<String, Object>>> getBlogCountByTag() throws Exception {
        return ResultModel.ok(blogService.getBlogCountByTag());
    }

    @LogByMethod("/admin/dashboard/getBlogCountByBlogSort")
    @ApiOperation(value = "获取每个分类下文章数目", notes = "获取每个分类下文章数目", response = Response.class)
    @GetMapping("/getBlogCountByBlogSort")
    public ResultModel<List<Map<String, Object>>> getBlogCountByBlogSort() throws Exception {
       return ResultModel.ok(blogService.getBlogCountByBlogSort());
    }

    @LogByMethod("/admin/dashboard/getBlogContributeCount")
    @ApiOperation(value = "获取一年内的文章贡献数", notes = "获取一年内的文章贡献度", response = Response.class)
    @RequestMapping(value = "/getBlogContributeCount", method = RequestMethod.GET)
    public ResultModel<Map<String, Object>> getBlogContributeCount() throws Exception{
        return ResultModel.ok(blogService.getBlogContributeCount());
    }

    @LogByMethod("/admin/dashboard/getWebVisitGroupByBehavior")
    @ApiOperation(value = "按月和天获取用户行为数据", notes = "按月和天获取用户行为数据", response = Response.class)
    @RequestMapping(value = "/getWebVisitGroupByBehavior", method = RequestMethod.GET)
    public ResultModel<Map<String, Object>> getWebVisitGroupByBehavior() throws Exception{
        return ResultModel.ok(webVisitService.getWebVisitGroupByBehavior());
    }
}
