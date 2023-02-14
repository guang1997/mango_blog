package com.myblog.service.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class PageUtil {

    public static <T> List<T> page(List<T> data, long page, long size) {
        if (CollectionUtils.isEmpty(data)) {
            return new ArrayList<>();
        }
        return data.stream().skip((page - 1) * size).limit(size).collect(Collectors.toList());
    }
}
