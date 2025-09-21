package com.farmapp.service.impl;

import com.farmapp.dto.request.User.UserCreateRequestDTO;
import com.farmapp.dto.request.User.UserUpdateRequestDTO;
import com.farmapp.dto.response.User.UserResponseDTO;
import com.farmapp.enums.UserRole;
import com.farmapp.exception.FarmAppException;
import com.farmapp.mapper.UserMapper;
import com.farmapp.repository.UserRepository;
import com.farmapp.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.farmapp.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllFarmers() {
        return userRepository.findAllByRoleAndActiveTrue(UserRole.FARMER).stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<UserResponseDTO> getById(Integer id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponse);
    }

    @Override
    public UserResponseDTO createFarmer(UserCreateRequestDTO dto) {
        // Kiểm tra trùng số điện thoại
        if (userRepository.findByPhoneAndActiveTrue(dto.getPhone()).isPresent()) {
            throw new FarmAppException("Số điện thoại đã được sử dụng", HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.toEntity(dto);
        user.setRole(UserRole.FARMER);
        user.setActive(true);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateFarmer(Integer id, UserUpdateRequestDTO updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));
        UserMapper.updateEntity(existing, updatedUser);
        return UserMapper.toResponse(userRepository.save(existing));
    }
    @Override
    public void deleteFarmer(Integer id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new FarmAppException("Không tìm thấy người dùng", HttpStatus.NOT_FOUND));
        existing.setActive(false);
        userRepository.save(existing);
    }
}
