package com.project.yonginsecretary.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TodoWriteDTO {

    @NotEmpty
    private String title;
    private String writer;
}
