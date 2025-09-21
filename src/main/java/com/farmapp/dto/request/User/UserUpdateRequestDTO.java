package com.farmapp.dto.request.User;


import com.farmapp.enums.UserGender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDTO {
    private String name;
    private String avatarUrl;
    private UserGender gender;
    private String phone;
    private String address;
    private String identityCardNumber;
    private String identityCardFrontUrl;
    private String identityCardBackUrl;
}
