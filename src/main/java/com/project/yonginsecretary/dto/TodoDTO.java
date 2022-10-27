package com.project.yonginsecretary.dto;

import com.project.yonginsecretary.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
