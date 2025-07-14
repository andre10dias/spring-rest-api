package com.github.andre10dias.spring_rest_api.controller.docs;

import com.github.andre10dias.spring_rest_api.data.dto.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmailControllerDocs {

    @Operation(
            summary = "Send an E-mail",
            description = "Sends an E-mail by proving details, subject and body.",
            tags = {"E-mail"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(
            summary = "Send an E-mail with attachment",
            description = "Sends an E-mail with attachment by proving details, subject and body.",
            tags = {"E-mail"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200"
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile);

}
