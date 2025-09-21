
package com.farmapp.dto.request.User;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO {
    private String name;
    private String phone;
    private String address;
    private String identityCard;
    private String identityCardUrl;
}
