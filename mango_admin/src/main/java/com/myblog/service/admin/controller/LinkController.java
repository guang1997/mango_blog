package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.entity.dto.WebConfigDto;
import com.myblog.service.admin.service.LinkService;
import com.myblog.service.admin.service.WebConfigService;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.TwoTuple;
import com.myblog.service.security.service.VerificationCodeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private WebConfigService webConfigService;

    @LogByMethod("/admin/link/getLinkByPage")
    @ApiOperation(value = "分页查询友情链接", notes = "分页查询友情链接", response = Response.class)
    @PostMapping("/getLinkByPage")
    public ResultModel<Map<String, Object>> getLinkByPage(@RequestBody LinkDto linkDto) throws Exception {
        return ResultModel.ok(linkService.getLinkByPage(linkDto));
    }

    @LogByMethod(value = "/admin/link/addLink", validate = true)
    @ApiOperation(value = "新增友情链接", notes = "新增友情链接", response = Response.class)
    @PostMapping("/addLink")
    public ResultModel<Object> addLink(@RequestBody @Validated LinkDto linkDto) throws Exception {
        if (linkService.addLink(linkDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }

    @LogByMethod(value = "/admin/link/editLink", validate = true)
    @ApiOperation(value = "修改友情链接", notes = "修改友情链接", response = Response.class)
    @PutMapping("/editLink")
    public ResultModel<Object> editLink(@RequestBody @Validated LinkDto linkDto) throws Exception {
        Boolean editSuccess = linkService.editLink(linkDto);
        // 如果是更改友链状态，更改成功后发送邮件
        if (BooleanUtils.isTrue(editSuccess) && BooleanUtils.isTrue(linkDto.getChangeStatus()) && Objects.equals(Constants.CommonStatus.ENABLED, linkDto.getLinkStatus())) {
            WebConfigDto webConfig = webConfigService.getWebConfig();
            editSuccess = verificationCodeService.sendEmail(linkDto.getEmail(), Constants.EmailSource.ADMIN, new TwoTuple<>(Constants.EmailParam.ADMIN_LINK, webConfig.getFriendLinkUrl()));
        }
        if (editSuccess) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }

    @LogByMethod("/admin/link/delLink")
    @ApiOperation(value = "删除友情链接", notes = "删除友情链接", response = Response.class)
    @DeleteMapping("/delLink")
    public ResultModel<Object> delLink(@RequestBody Set<String> ids) throws Exception {
        if (linkService.delLink(ids)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
    }
}

