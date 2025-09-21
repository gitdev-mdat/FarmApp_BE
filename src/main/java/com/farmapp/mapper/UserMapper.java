package com.farmapp.mapper;

import com.farmapp.dto.request.User.UserCreateRequestDTO;
import com.farmapp.dto.request.User.UserUpdateRequestDTO;
import com.farmapp.dto.response.User.UserResponseDTO;
import com.farmapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    // Tạo user
    public static User toEntity (UserCreateRequestDTO dto) {
        return User.builder()
                .name(dto.getName())
                .fakeName(dto.getFakeName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .identityCard(dto.getIdentityCard())
                .isActive(true)
                .identityCardUrl(dto.getIdentityCardUrl())
                .build();
    }
    // Cập nhật User
    public static void  updateEntity (User user, UserUpdateRequestDTO dto) {
        user.setName(dto.getName());
        user.setFakeName(dto.getFakeName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setIdentityCard(dto.getIdentityCard());
        user.setIdentityCardUrl(dto.getIdentityCardUrl());
    }
    public static UserResponseDTO toResponse (User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .fakeName(user.getFakeName())
                .identityCard(user.getIdentityCard())
                .identityCardUrl(user.getIdentityCardUrl())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }
    // Trả về danh sách User
    public static List<UserResponseDTO> toResponseList (List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());

    }


}
