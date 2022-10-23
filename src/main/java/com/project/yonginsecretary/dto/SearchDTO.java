package com.project.yonginsecretary.dto;

import lombok.Data;

@Data
public class SearchDTO {

    // 1.제목  2.작성자  3.좋아요  4.최신순
    private int opt;
    private String str;
}
