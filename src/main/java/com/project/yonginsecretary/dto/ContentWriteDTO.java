package com.project.yonginsecretary.dto;

import com.project.yonginsecretary.entity.UploadFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ContentWriteDTO {

    @NotEmpty
    private String title;
    private String texts;

    private String writer;
    private int category;

    private List<MultipartFile> imageFiles;
    // 게시물 수정 시에 현재 이미지 파일 보여주는 변수
    private List<UploadFile> nowImageFiles;
}
