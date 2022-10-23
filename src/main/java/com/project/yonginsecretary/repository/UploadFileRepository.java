package com.project.yonginsecretary.repository;

import com.project.yonginsecretary.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    //컨텐츠 수정 시 해당 ContentID를 통해 다 지워주는 작업 필요.
    void deleteAllByContentId(Long contentId);
}