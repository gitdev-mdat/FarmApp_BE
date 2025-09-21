package com.farmapp.service.interfaces;

import com.farmapp.dto.request.User.UserCreateRequestDTO;
import com.farmapp.dto.request.User.UserUpdateRequestDTO;
import com.farmapp.dto.response.User.UserResponseDTO;
import com.farmapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> getAllFarmers();

    Optional<UserResponseDTO> getById(Integer id);

    UserResponseDTO createFarmer(UserCreateRequestDTO dto);

    UserResponseDTO updateFarmer(Integer id, UserUpdateRequestDTO dto);

    void deleteFarmer(Integer id);

}
