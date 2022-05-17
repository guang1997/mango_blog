package com.myblog.service.admin.service;

import com.myblog.service.base.common.Response;

public interface VerificationCodeService {
    Response sendCode(String email);

    Response validateCode(String email, String code);
}
