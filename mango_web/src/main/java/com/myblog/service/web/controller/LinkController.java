package com.myblog.service.web.controller;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.TwoTuple;
import com.myblog.service.security.service.VerificationCodeService;
import com.myblog.service.web.entity.dto.LinkDto;
import com.myblog.service.web.service.LinkService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-09
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/web/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private VerificationCodeService verificationCodeService;


    private static final ExecutorService sendEmailExecutor = new ThreadPoolExecutor(
            1,
            1,
            120,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("link-send-email").build()
    );

    @LogByMethod(value = "/web/link/getFriendLink", behavior = BehaviorEnum.FRIENDSHIP_LINK)
    @ApiOperation(value = "查询友链信息", notes = "查询友链信息", response = Response.class)
    @PostMapping("/getFriendLink")
    public ResultModel<List<LinkDto>> getFriendLink(@RequestBody LinkDto linkDto) throws Exception {
        return ResultModel.ok(linkService.getFriendLink(linkDto));
    }

    @LogByMethod("/web/link/saveFriendLink")
    @ApiOperation(value = "保存友链", notes = "保存友链", response = Response.class)
    @PostMapping("/saveFriendLink")
    public ResultModel<Object> saveFriendLink(@RequestBody LinkDto linkDto) throws Exception {
        // 校验验证码
        if (!verificationCodeService.validateCode(linkDto.getEmail(), linkDto.getCode(), Constants.EmailSource.WEB)) {
            return ResultModel.error().message("验证码错误");
        }
        // 保存成功之后异步发送邮件
        if (linkService.saveFriendLink(linkDto)) {
            this.sendEmail(linkDto.getEmail(), linkDto.getUrl());
        }
        return ResultModel.ok();
    }

    private void sendEmail(String email, String url) {
        sendEmailExecutor.submit(new SendEmailTask(email, url));
    }
    
    class SendEmailTask implements Runnable {
        private String email;

        private String url;

        public SendEmailTask() {
        }

        public SendEmailTask(String email, String url) {
            this.email = email;
            this.url = url;
        }

        @Override
        public void run() {
            try {
                verificationCodeService.sendEmail(email, Constants.EmailSource.WEB, new TwoTuple<>(Constants.EmailParam.WEB_LINK, url));
            } catch (Exception e) {
                log.error("saveFriendLink sendEmail failed, exception:", e);
            }
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

