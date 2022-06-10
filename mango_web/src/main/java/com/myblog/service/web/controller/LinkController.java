package com.myblog.service.web.controller;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.TwoTuple;
import com.myblog.service.security.service.VerificationCodeService;
import com.myblog.service.web.entity.dto.LinkDto;
import com.myblog.service.web.service.LinkService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
@RestController
@RequestMapping("/web/link")
public class LinkController {

    private static Logger LOGGER = LoggerFactory.getLogger(LinkController.class);

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

    @LogByMethod("/web/link/getFriendLink")
    @ApiOperation(value = "查询友链信息", notes = "查询友链信息", response = Response.class)
    @PostMapping("/getFriendLink")
    public Response getFriendLink(@RequestBody LinkDto linkDto) throws Exception {
        return linkService.getFriendLink(linkDto);
    }

    @LogByMethod("/web/link/saveFriendLink")
    @ApiOperation(value = "保存友链", notes = "保存友链", response = Response.class)
    @PostMapping("/saveFriendLink")
    public Response saveFriendLink(@RequestBody LinkDto linkDto) throws Exception {
        // 校验验证码
        if (!verificationCodeService.validateCode(linkDto.getEmail(), linkDto.getCode(), Constants.EmailSource.WEB).getSuccess()) {
            return Response.error().message("验证码错误");
        }
        Response response = linkService.saveFriendLink(linkDto);
        // 保存成功之后异步发送邮件
        if (response.getSuccess()) {
            this.sendEmail(linkDto.getEmail(), linkDto.getUrl());
        }
        return response;
    }

    private void sendEmail(String email, String url) {
        sendEmailExecutor.submit(new sendEmailTask(email, url));
    }
    
    class sendEmailTask implements Runnable {
        private String email;

        private String url;

        public sendEmailTask() {
        }

        public sendEmailTask(String email, String url) {
            this.email = email;
            this.url = url;
        }

        @Override
        public void run() {
            try {
                verificationCodeService.sendEmail(email, Constants.EmailSource.WEB, new TwoTuple<>(Constants.EmailParam.WEB_LINK, url));
            } catch (Exception e) {
                LOGGER.error("saveFriendLink sendEmail failed, exception:", e);
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

