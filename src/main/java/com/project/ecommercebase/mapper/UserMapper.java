package com.project.ecommercebase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.dto.request.UserRegisterRequest;
import com.project.ecommercebase.dto.request.UserUpdateRequest;
import com.project.ecommercebase.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    void registerUser(@MappingTarget User user, UserRegisterRequest userRegisterRequest);

    void updaterUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    UserResponse userToUserResponse(User user);
}
