package com.github.andre10dias.spring_rest_api.service;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PermissionDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v1.UserDTO;
import com.github.andre10dias.spring_rest_api.exception.RequiredObjectIsNullException;
import com.github.andre10dias.spring_rest_api.model.Permission;
import com.github.andre10dias.spring_rest_api.model.User;
import com.github.andre10dias.spring_rest_api.repository.PermissionRepository;
import com.github.andre10dias.spring_rest_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.andre10dias.spring_rest_api.mapper.ObjectMapper.parseObject;
import static com.github.andre10dias.spring_rest_api.service.AuthService.generatedHashPassword;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public UserDTO create(UserDTO userDto) {
        logger.info("create: {}", userDto);
        if (userDto == null) throw new RequiredObjectIsNullException();

        var user = parseObject(userDto, User.class);

        user.setPassword(generatedHashPassword(userDto.getPassword()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        // Recuperar permissões por ID
        if (userDto.getPermissions() != null && !userDto.getPermissions().isEmpty()) {
            List<Long> permissionIds = userDto.getPermissions().stream()
                    .map(PermissionDTO::getId)
                    .toList();

            List<Permission> permissions = permissionRepository.findAllById(permissionIds);
            user.setPermissions(permissions);
        }

        return parseObject(userRepository.save(user), UserDTO.class);
    }


}