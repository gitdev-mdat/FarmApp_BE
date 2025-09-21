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
                .avatarUrl(dto.getAvatarUrl())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .identityCardNumber(dto.getIdentityCardNumber())
                .identityCardFrontUrl(dto.getIdentityCardFrontUrl())
                .identityCardBackUrl(dto.getIdentityCardBackUrl())
                .build();
    }
    // Cập nhật User
    public static void  updateEntity (User user, UserUpdateRequestDTO dto) {
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setGender(dto.getGender());
        user.setIdentityCardNumber(dto.getIdentityCardNumber());
        user.setIdentityCardFrontUrl(dto.getIdentityCardFrontUrl());
        user.setIdentityCardBackUrl(dto.getIdentityCardBackUrl());
    }
    public static UserResponseDTO toResponse (User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .phone(user.getPhone())
                .address(user.getAddress())
                .identityCardNumber(user.getIdentityCardNumber())
                .identityCardFrontUrl(user.getIdentityCardFrontUrl())
                .identityCardBackUrl(user.getIdentityCardBackUrl())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
    // Trả về danh sách User
    public static List<UserResponseDTO> toResponseList (List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());

    }


}
