package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class UserService {
    @Autowired
    private UserRepository repository;

    public UserResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }


        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User userSaved = repository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userSaved.getId());
        userResponse.setEmail(userSaved.getEmail());
        userResponse.setPassword(userSaved.getPassword());
        userResponse.setFirstName(userSaved.getFirstName());
        userResponse.setLastName(userSaved.getLastName());
        userResponse.setCreatedAt(userSaved.getCreatedAt());
        userResponse.setUpdatedAt(userSaved.getUpdatedAt());

        return userResponse;


    }

    public UserResponse getUserProfile(String userId){
        User user = repository.findById(userId).
                orElseThrow(() -> new RuntimeException("User Not Found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    public Boolean existByUserId(String userId) {
        log.info("Calling userValidation API for userId:{}", userId);
        return repository.existsById(userId);
    }
}
