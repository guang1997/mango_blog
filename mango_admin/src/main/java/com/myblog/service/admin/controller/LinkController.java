package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.service.LinkService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.TwoTuple;
import com.myblog.service.security.service.VerificationCodeService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-24
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/link")
public class LinkController {

    private static Logger LOGGER = LoggerFactory.getLogger(LinkController.class);

    @Autowired
    private LinkService linkService;

    @Value("${email.blogAddress}")
    private String blogAddress;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @LogByMethod("/admin/link/getLinkByPage")
    @ApiOperation(value = "分页查询友情链接", notes = "分页查询友情链接", response = Response.class)
    @PostMapping("/getLinkByPage")
    public Response getLinkByPage(@RequestBody LinkDto linkDto) throws Exception {
        return linkService.getLinkByPage(linkDto);
    }

    @LogByMethod(value = "/admin/link/addLink", validate = true)
    @ApiOperation(value = "新增友情链接", notes = "新增友情链接", response = Response.class)
    @PostMapping("/addLink")
    public Response addLink(@RequestBody LinkDto linkDto) throws Exception {
        if (StringUtils.isBlank(linkDto.getUrl()) || StringUtils.isBlank(linkDto.getTitle())) {
            LOGGER.error("addLink failed, url or title cannot be null, link:{}", linkDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return linkService.addLink(linkDto);
    }

    @LogByMethod(value = "/admin/link/editLink", validate = true)
    @ApiOperation(value = "修改友情链接", notes = "修改友情链接", response = Response.class)
    @PutMapping("/editLink")
    public Response editLink(@RequestBody LinkDto linkDto) throws Exception {
        Response response = linkService.editLink(linkDto);
        // 如果是更改友链状态，更改成功后发送邮件
        if (response.getSuccess() && BooleanUtils.isTrue(linkDto.getChangeStatus()) && Objects.equals(Constants.CommonStatus.ENABLED, linkDto.getLinkStatus())) {
            response = verificationCodeService.sendEmail(linkDto.getEmail(), Constants.EmailSource.ADMIN, new TwoTuple<>(Constants.EmailParam.ADMIN_LINK, blogAddress));
        }
        return response;
    }

    @LogByMethod("/admin/link/delLink")
    @ApiOperation(value = "删除友情链接", notes = "删除友情链接", response = Response.class)
    @DeleteMapping("/delLink")
    public Response delLink(@RequestBody Set<String> ids) throws Exception {
        return linkService.delLink(ids);
    }
}

