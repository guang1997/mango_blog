package com.myblog.service.admin.entity.vo;

import com.myblog.service.base.entity.vo.BaseVO;
import lombok.Data;

import java.util.List;

@Data
public class DictTypeVO extends BaseVO {

    private List<String> dictTypes;

    private String dictType;
}
