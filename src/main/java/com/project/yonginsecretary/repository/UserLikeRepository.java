package com.project.yonginsecretary.repository;

import com.project.yonginsecretary.entity.Content;
import com.project.yonginsecretary.entity.UserLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {

    @Query(
            value = "select c from Content c join c.userLikes u where u.user.id = :userId"
    )
    Page<Content> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = "select c from Content c join c.userLikes u where u.user.id = :userId and "
                    + "c.user.nickname = :userNickname"
    )
    Page<Content> findByUserIdAndUserNickname(@Param("userId") Long userId,
                                              @Param("userNickname") String userNickname, Pageable pageable);

    @Query(
            value = "select c from Content c join c.userLikes u where u.user.id = :userId and "
                    + "c.title like %:contentTitle%"
    )
    Page<Content> findByUserIdAndContentTitleContains(@Param("userId") Long userId,
                                                      @Param("contentTitle") String contentTitle, Pageable pageable);

    Optional<UserLike> findByUserIdAndContentId(Long userId, Long content_Id);
}