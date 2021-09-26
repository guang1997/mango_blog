package com.myblog.service.admin.service.impl;

import com.myblog.service.admin.entity.Blog;
import com.myblog.service.admin.mapper.BlogMapper;
import com.myblog.service.admin.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
