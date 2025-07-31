package com.github.andre10dias.spring_rest_api.controller;


import com.github.andre10dias.spring_rest_api.controller.docs.UserControllerDocs;
import com.github.andre10dias.spring_rest_api.data.dto.v1.UserDTO;
import com.github.andre10dias.spring_rest_api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/v1")
@Tag(name = "User", description = "Endpoints for Managing Users.")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public UserDTO create(@RequestBody UserDTO user) {
        return userService.create(user);
    }

}
