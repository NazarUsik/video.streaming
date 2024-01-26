package com.test.task.account.mapper;

import com.test.task.account.config.MapStructConfig;
import com.test.task.account.data.entity.User;
import com.test.task.account.model.api.UserDto;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    User mapFromDto(UserDto user);

    UserDto mapToDto(User user);

    default List<UserDto> mapToDto(List<User> users) {
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    default List<User> mapFromDto(List<UserDto> users) {
        return users.stream().map(this::mapFromDto).collect(Collectors.toList());
    }
}
