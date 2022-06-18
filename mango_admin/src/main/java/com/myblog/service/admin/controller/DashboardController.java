package com.myblog.service.admin.controller;

import com.myblog.service.admin.service.BlogService;
import com.myblog.service.admin.service.CommentService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.WebVisitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/admin/dashboard")
@Api(value = "首页相关接口", tags = {"首页相关接口"})
public class DashboardController {

    private static Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private WebVisitService webVisitService;

    @Autowired
    private CommentService commentService;

    @LogByMethod("/admin/dashboard/init")
    @ApiOperation(value = "首页初始化数据", notes = "首页初始化数据", response = Response.class)
    @GetMapping("/init")
    public Response init() throws Exception {
        Response response = Response.ok();
        int commentCount = commentService.getCommentCount(Constants.CommentStatus.REVIEWED);
        int blogCount = blogService.getBlogCount();
        int webVisitCount = webVisitService.getWebVisitCount();
        int adminCount = adminService.getAdminCount();

        response.data(Constants.ReplyField.COMMENT_COUNT, commentCount)
                .data(Constants.ReplyField.BLOG_COUNT, blogCount)
                .data(Constants.ReplyField.VISIT_COUNT, webVisitCount)
                .data(Constants.ReplyField.USER_COUNT, adminCount);
        return response;
    }

    @LogByMethod("/admin/dashboard/getVisitByWeek")
    @ApiOperation(value = "获取最近一周用户访问量", notes = "获取最近一周用户访问量", response = Response.class)
    @GetMapping("/getVisitByWeek")
    public Response getVisitByWeek() throws Exception {
        return webVisitService.getVisitByWeek();
    }

    @LogByMethod("/admin/dashboard/getBlogCountByTag")
    @ApiOperation(value = "获取每个标签下文章数目", notes = "获取每个标签下文章数目", response = Response.class)
    @GetMapping("/getBlogCountByTag")
    public Response getBlogCountByTag() throws Exception {
        return blogService.getBlogCountByTag();
    }

    @LogByMethod("/admin/dashboard/getBlogCountByBlogSort")
    @ApiOperation(value = "获取每个分类下文章数目", notes = "获取每个分类下文章数目", response = Response.class)
    @GetMapping("/getBlogCountByBlogSort")
    public Response getBlogCountByBlogSort() throws Exception {
       return blogService.getBlogCountByBlogSort();
    }

    @LogByMethod("/admin/dashboard/getBlogContributeCount")
    @ApiOperation(value = "获取一年内的文章贡献数", notes = "获取一年内的文章贡献度", response = Response.class)
    @RequestMapping(value = "/getBlogContributeCount", method = RequestMethod.GET)
    public Response getBlogContributeCount() throws Exception{
        return blogService.getBlogContributeCount();
    }

    @LogByMethod("/admin/dashboard/getWebVisitGroupByBehavior")
    @ApiOperation(value = "按月和天获取用户行为数据", notes = "按月和天获取用户行为数据", response = Response.class)
    @RequestMapping(value = "/getWebVisitGroupByBehavior", method = RequestMethod.GET)
    public Response getWebVisitGroupByBehavior() throws Exception{
        return webVisitService.getWebVisitGroupByBehavior();
    }
}
