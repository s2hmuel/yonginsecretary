package com.project.yonginsecretary.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserEditDTO {
    private String loginId;
    private String oldNickname;
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordCheck;
    @NotEmpty
    private String nickname;
}
