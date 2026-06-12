package org.example;

public class UserMapping {
    UserDto toDto(User user) {
        return new UserDto(user.getName(), user.getYear());
    }

    User toEntity(UserDto user) {
        return new User(null, user.getName(), user.getYear());
    }
}
