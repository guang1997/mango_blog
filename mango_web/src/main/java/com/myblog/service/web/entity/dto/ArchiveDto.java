package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArchiveDto extends BaseReqDto {

    private String title;

    private String fileId;

    private Boolean queryByMonth;

    private String year;

    private String month;

    private Integer blogNum;

    List<ArchiveDto> childrens = new ArrayList<>();
}
