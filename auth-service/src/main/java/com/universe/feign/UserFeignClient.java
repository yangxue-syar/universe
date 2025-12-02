package com.universe.feign;

import com.universe.common.Result;
import com.universe.user.dto.AssignRoleDTO;
import com.universe.utils.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/admin/user/list")
    Result<List<AssignRoleDTO>> list();
    @GetMapping("/admin/user/{id}")
    Result<AssignRoleDTO> get(@PathVariable("id") Long id);

    @PostMapping("/admin/user")
    Result<Void> assignRoles(AssignRoleDTO dto);
}
