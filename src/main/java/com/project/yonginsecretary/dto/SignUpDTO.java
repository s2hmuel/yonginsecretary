package com.project.yonginsecretary.dto;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpDTO {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordCheck;
    @NotEmpty
    private String nickname;
}
