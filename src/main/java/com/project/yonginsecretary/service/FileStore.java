package com.project.yonginsecretary.service;
import com.project.yonginsecretary.entity.Content;
import com.project.yonginsecretary.entity.UploadFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    private final String fileDir = "D:/개발공부/SpringStudy/Board-dbx/files/";

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public UploadFile storeFile(MultipartFile multipartFile, Content content) throws IOException {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // ( 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로 )
        String storeFilename = UUID.randomUUID().toString() + "." + extractExt(originalFilename);

        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장
        multipartFile.transferTo(new File(getFullPath(storeFilename)));

        UploadFile uploadFile = new UploadFile();
        uploadFile.setUploadFilename(originalFilename);
        uploadFile.setStoreFilename(storeFilename);
        uploadFile.setContent(content);

        return uploadFile;
    }

    // 파일이 여러개 들어왔을 때 처리해주는 부분
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, Content content) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, content));
            }
        }
        return storeFileResult;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}