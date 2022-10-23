package com.project.yonginsecretary.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;
    private String title;
    private String uploadDate;
    private boolean completed;
    @ManyToOne(fetch = FetchType.LAZY) //지연 입력
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
        user.getTodoes().add(this);
    }
}
