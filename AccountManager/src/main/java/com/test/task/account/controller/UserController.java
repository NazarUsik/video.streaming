package com.test.task.account.controller;

import com.test.task.account.data.entity.User;
import com.test.task.account.data.service.UserService;
import com.test.task.account.mapper.UserMapper;
import com.test.task.account.model.api.UserDto;
import com.test.task.account.model.security.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.login(user));
    }

    @PostMapping("/registration")
    public ResponseEntity<AccessToken> registration(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.registration(user));
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> info(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(userMapper
                .mapToDto(userService.byId(user.getId())));
    }

    @GetMapping("/byId/{userId}")
    public ResponseEntity<UserDto> info(@PathVariable String userId) {
        return ResponseEntity.ok().body(userMapper
                .mapToDto(userService.byId(userId)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> info() {
        return ResponseEntity.ok().body(userMapper
                .mapToDto(userService.all()));
    }
}
