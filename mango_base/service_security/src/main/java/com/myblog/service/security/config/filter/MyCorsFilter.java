package com.myblog.service.security.config.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
public class MyCorsFilter extends CorsFilter {

    public MyCorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }
}
