package com.universe.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;

    private String usename;

    private String password;

    private String email;

    private String phone;

    private Integer status;

    private Integer isSAdmin;

    private List<Long> roleIds;

}
