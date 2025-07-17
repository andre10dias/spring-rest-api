package com.github.andre10dias.spring_rest_api.controller;


import com.github.andre10dias.spring_rest_api.data.dto.v1.UserDTO;
import com.github.andre10dias.spring_rest_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO create(@RequestBody UserDTO user) {
        return userService.create(user);
    }

}
