package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMappingTest {
    private  UserMapping sut = new UserMapping();

    @Test
    void toDto() {
        User user = new User("1", "Max", 12);
        UserDto expected = new UserDto("Max", 12);

        UserDto actual = sut.toDto(user);

        assertEquals(actual, expected);
    }

    @Test
    void toEntity() {
        UserDto userDto = new UserDto("Max", 12);
        User expected = new User(null, "Max", 12);

        User actual = sut.toEntity(userDto);

        assertEquals(actual, expected);
    }
}