package com.project.yonginsecretary.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uploadFile_id")
    private Long id;
    private String uploadFilename; // 글 작성 및 수정 할 때, 첨부한 이미지 파일의 이름
    private String storeFilename;  // 서버 내부에서 관리하는 파일명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    public void setContent(Content content) {
        this.content = content;
        content.getImageFiles().add(this);
    }
}