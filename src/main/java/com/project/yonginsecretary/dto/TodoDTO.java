package com.project.yonginsecretary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TodoDTO {

    private Long id;
    private String title;
    private String uploadDate;
    private boolean completed;
    private String writer;
}
