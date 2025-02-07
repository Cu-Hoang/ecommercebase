package com.project.ecommercebase.mapper;

import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.project.ecommercebase.data.entity.User;
import com.project.ecommercebase.dto.request.UserRegisterRequest;
import com.project.ecommercebase.dto.request.UserUpdateRequest;
import com.project.ecommercebase.dto.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    void registerUser(@MappingTarget User user, UserRegisterRequest userRegisterRequest);

    void updaterUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    @Mapping(target = "firstname", qualifiedByName = "mapFirstname")
    @Mapping(target = "lastname", qualifiedByName = "mapLastname")
    @Mapping(target = "username", source = "user", qualifiedByName = "mapUsername")
    @Mapping(target = "dateOfBirth", qualifiedByName = "mapDateOfBirth")
    @Mapping(target = "avatar", qualifiedByName = "mapAvatar")
    UserResponse userToUserResponse(User user);

    @Named("mapFirstname")
    default String getFirstname(String firstname) {
        return firstname == null ? "" : firstname;
    }

    @Named("mapLastname")
    default String getLastname(String lastname) {
        return lastname == null ? "" : lastname;
    }

    @Named("mapUsername")
    default String getUsername(User user) {
        return user.getUsername() == null ? user.getEmail() : user.getUsername();
    }

    @Named("mapDateOfBirth")
    default LocalDate getDateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth == null ? LocalDate.of(1900, 1, 1) : dateOfBirth;
    }

    @Named("mapAvatar")
    default String getAvatar(String avatar) {
        return avatar == null ? "" : avatar;
    }
}
