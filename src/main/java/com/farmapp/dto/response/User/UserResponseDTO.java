package com.farmapp.dto.response.User;

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
    private String fakeName;
    private String phone;
    private String address;
    private String identityCard;
    private String identityCardUrl;
    private UserRole role;
    private boolean isActive;


}
