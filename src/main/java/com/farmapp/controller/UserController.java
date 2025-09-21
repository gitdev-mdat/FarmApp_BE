package com.farmapp.controller;


import com.farmapp.dto.request.User.UserCreateRequestDTO;
import com.farmapp.dto.request.User.UserUpdateRequestDTO;
import com.farmapp.dto.response.User.UserResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.mapper.UserMapper;
import com.farmapp.model.User;
import com.farmapp.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/farmers")
    public ResponseEntity<GlobalResponse<List<UserResponseDTO>>> getAllFarmers () {
        List<UserResponseDTO> users = userService.getAllFarmers();
        return ResponseEntity.ok(
                GlobalResponse.<List<UserResponseDTO>>builder()
                        .status(200)
                        .message("Lấy danh sách thành công")
                        .data(users)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<UserResponseDTO>> createUser ( @Valid @RequestBody UserCreateRequestDTO dto) {
        UserResponseDTO created = userService.createFarmer(dto);
        return ResponseEntity.ok(
                GlobalResponse.<UserResponseDTO>builder()
                        .status(201)
                        .message("Tạo farmer thành công")
                        .data(created)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<UserResponseDTO>> updateUser (
            @PathVariable Integer id,
            @RequestBody UserUpdateRequestDTO dto
    ) {
        UserResponseDTO updated = userService.updateFarmer(id, dto);
        return ResponseEntity.ok(
                GlobalResponse.<UserResponseDTO>builder()
                        .status(200)
                        .message("Cập nhật thành công")
                        .data(updated)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteUser (@PathVariable Integer id) {
        userService.deleteFarmer(id);
        return ResponseEntity.ok(
                GlobalResponse.<String>builder()
                        .status(200)
                        .message("Xoá mềm thành công")
                        .build()
        );
    }
}


