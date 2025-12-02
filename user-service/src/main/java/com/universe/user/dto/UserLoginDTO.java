package com.universe.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.util.pattern.PathPattern;

@Data
public class UserLoginDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
