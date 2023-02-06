package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.TwoTuple;

public interface VerificationCodeService {
    Boolean sendCode(String email, String source);

    Response validateCode(String email, String code, String source);

    Boolean sendEmail(String email, String source, TwoTuple<String, Object> param);
}
