package com.project.ecommercebase.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.dto.request.UserRequest;
import com.project.ecommercebase.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "username", expression = "java(userRequest.getUsername().toLowerCase())")
    @Mapping(target = "email", expression = "java(userRequest.getEmail().toLowerCase())")
    User toUser(UserRequest userRequest);

    UserResponse userToUserResponse(User user);
}
