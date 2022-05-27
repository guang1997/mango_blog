package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;

public interface VerificationCodeService {
    Response sendCode(String email, String source);

    Response validateCode(String email, String code, String source);
}
