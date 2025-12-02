package com.universe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserUpateDTO {
    @NotBlank(message = "用户名不能为空")
    private Long id;

    private String nickname;

    private String avatar;

    private String email;

    private String phone;

    private Integer status;

    private String isAmin;

    private List<Long> roleIds;
}
