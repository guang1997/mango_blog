package com.myblog.service.admin.entity.vo;

import com.myblog.service.base.entity.vo.BaseVo;
import lombok.Data;

import java.util.List;

@Data
public class DictTypeVo extends BaseVo {

    private List<String> dictTypes;

    private String dictType;
}
