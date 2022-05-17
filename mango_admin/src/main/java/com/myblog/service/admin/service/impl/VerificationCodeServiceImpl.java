package com.myblog.service.admin.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailException;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.myblog.service.admin.entity.EmailConfig;
import com.myblog.service.admin.service.EmailConfigService;
import com.myblog.service.admin.service.VerificationCodeService;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.Base64Util;
import com.myblog.service.base.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static Logger LOGGER = LoggerFactory.getLogger(VerificationCodeServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailConfigService emailConfigService;

    @Value("${email.code.expiresSecond}")
    private Long expiresSecond;

    /**
     * 给邮箱发送验证码
     * @param email
     * @return
     */
    @Override
    public Response sendCode(String  email) {

        String key = RedisConstants.EMAIL_CODE + RedisConstants.DIVISION + email;
        String content = "";
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("template/email/email.ftl");
        String redisCode = redisUtil.get(key);
        // 先判断redis中是否已经有验证码，有验证码直接使用
        if (StringUtils.isNotBlank(redisCode)) {
            content = template.render(Dict.create().set("code",redisCode));
        } else {
            // redis中没有则生成验证码
            String code = RandomUtil.randomNumbers (6);
            // 将验证码放到redis中
            redisUtil.setEx(key, code, expiresSecond, TimeUnit.MILLISECONDS);
            content = template.render(Dict.create().set("code",code));
        }

        // 发送验证码
        EmailConfig emailConfig = emailConfigService.getOne(null);
        if (Objects.isNull(emailConfig)) {
            return Response.error().message("发送邮件失败，请联系管理员配置邮箱");
        }
        MailAccount account = new MailAccount();
        // 设置用户
        account.setUser(emailConfig.getUser());
        account.setHost(emailConfig.getHost());
        account.setPort(emailConfig.getPort());
        account.setAuth(true);
        account.setPass(Base64Util.decodeToString(emailConfig.getPassword()));
        account.setFrom(emailConfig.getFromUser());
        // ssl方式发送
//        account.setSslEnable(true);
        // 使用STARTTLS安全连接
//        account.setStarttlsEnable(true);
        try {
            Mail.create(account)
                    .setTos(email)
                    .setTitle(emailConfig.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (MailException e) {
            // 发送失败从redis中删除验证码
            redisUtil.delete(key);
            LOGGER.error("sendCode failed, exception:", e);
            return Response.setResult(ResultCodeEnum.SEND_CODE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 校验验证码
     * @param email
     * @param code
     * @return
     */
    @Override
    public Response validateCode(String email, String code) {
        String key = RedisConstants.EMAIL_CODE + RedisConstants.DIVISION + email;
        String redisCode = redisUtil.get(key);
        if (StringUtils.isBlank(redisCode) || !Objects.equals(redisCode, code)) {
            LOGGER.debug("validateCode, redisCode:[{}] is not equals code:[{}]", redisCode, code);
            return Response.setResult(ResultCodeEnum.CODE_ERROR);
        }
        redisUtil.delete(key);
        return Response.ok();
    }
}
