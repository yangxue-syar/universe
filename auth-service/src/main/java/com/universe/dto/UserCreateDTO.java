package com.universe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserCreateDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

    private  String avatar;

    private  String email;
    private  String phone;

    private Integer status=1;

    private Integer isSAdmin = 0;

    private List<Long> roleIds;
}
