package com.github.andre10dias.spring_rest_api.controller.docs;

import com.github.andre10dias.spring_rest_api.data.dto.security.AccountCredentialsDTO;
import com.github.andre10dias.spring_rest_api.data.dto.security.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user using username and password, returning an access token (JWT) and a refresh token.",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(
                            description = "Successfully authenticated. Returns an access token and a refresh token.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TokenDTO.class))
                    ),
                    @ApiResponse(
                            description = "Invalid request. Missing username or password.",
                            responseCode = "400",
                            content = @Content
                    ),
                    @ApiResponse(
                            description = "Invalid credentials or user not found.",
                            responseCode = "401",
                            content = @Content
                    )
            }
    )
    ResponseEntity<TokenDTO> signIn(@RequestBody AccountCredentialsDTO credentials);

}