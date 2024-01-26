package com.test.task.account.data.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.task.account.client.AuthorizationManagerClient;
import com.test.task.account.data.entity.User;
import com.test.task.account.data.repository.UserRepository;
import com.test.task.account.mapper.UserMapper;
import com.test.task.account.model.security.AccessToken;
import com.test.task.account.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final ObjectMapper mapper;
    private final AuthorizationManagerClient authMgrClient;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;

    public User save(User user) {
        UUID uuid = UUID.randomUUID();
        user.setId(uuid.toString());
        user.setPassword(encoder.encode(user.getPassword()));

        repository.save(user);
        return user;
    }

    public User byId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User id is incorrect"));
    }

    public List<User> all() {
        return repository.findAll();
    }

    @Transactional
    public AccessToken login(User user) {
        userValidator.validate(user);

        Optional<User> loadedUserOptional = repository.findByEmail(user.getEmail());
        if (loadedUserOptional.isPresent()) {
            User loadedUser = loadedUserOptional.get();

            boolean matches = encoder.matches(user.getPassword(), loadedUser.getPassword());
            if (matches) {
                return generateTokenForUser(loadedUser);
            }

            throw new IllegalArgumentException("Password is incorrect");
        }

        throw new IllegalArgumentException("Email doesn't exist");
    }

    @Transactional
    public AccessToken registration(User user) {
        userValidator.validate(user);

        Optional<User> loadedUserOptional = repository.findByEmail(user.getEmail());
        if (loadedUserOptional.isEmpty()) {
            return generateTokenForUser(this.save(user));
        }

        throw new IllegalArgumentException("Email is already exist");
    }

    private AccessToken generateTokenForUser(User user) {
        Map<String, Object> payload = mapper.convertValue(userMapper.mapToDto(user), new TypeReference<>() {
        });

        return authMgrClient.sign(payload);
    }


}
