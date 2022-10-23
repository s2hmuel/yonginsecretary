package com.project.yonginsecretary.repository;

import com.project.yonginsecretary.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
    //게시판 별로 다른 글 불러오기
    Page<Content> findByCategory(Integer category, Pageable pageable);
    //검색기능
    Page<Content> findByCategoryAndUserNickname(Integer category, String userNickname, Pageable pageable);
    Page<Content> findByCategoryAndTitleContains(Integer category, String title, Pageable pageable);
}