package com.github.andre10dias.spring_rest_api.controller.docs;

import com.github.andre10dias.spring_rest_api.data.dto.v1.PersonDTO;
import com.github.andre10dias.spring_rest_api.data.dto.v1.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerDocs {

    @Operation(
            summary = "Create user",
            description = "Create a new user.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = PersonDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    UserDTO create(@RequestBody UserDTO user);

}
