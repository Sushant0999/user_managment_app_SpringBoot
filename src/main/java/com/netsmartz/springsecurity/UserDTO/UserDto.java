package com.netsmartz.springsecurity.UserDTO;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDto {
    private Integer id;

    private String fullname;

    private String email;

    private String address;

    private String qualification;

    private String role;
}
