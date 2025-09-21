package com.farmapp.dto.response.User;

import com.farmapp.enums.UserGender;
import com.farmapp.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String avatarUrl;
    private UserGender gender;
    private String phone;
    private String address;
    private String identityCardNumber;
    private String identityCardFrontUrl;
    private String identityCardBackUrl;
    private UserRole role;
    private boolean active;


}
