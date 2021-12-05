package com.myblog.service.security.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    private List<String> roles;

    private String title;

    private String icon;
}
