package com.github.andre10dias.spring_rest_api.controller.docs;

import com.github.andre10dias.spring_rest_api.data.dto.v1.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookControllerDocs {

    @Operation(
            summary = "Find all book",
            description = "Finds all book.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    List<BookDTO> findAll();

    @Operation(
            summary = "Find book by id",
            description = "Find a specific book by your ID.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(description = "No content", responseCode = "204"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    BookDTO findById(@PathVariable("id") Long id);

    @Operation(
            summary = "Create book",
            description = """
                    Create a new book by passing in a JSON, XML or YAML representation
                    of the book.
                    """,
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    BookDTO create(@RequestBody BookDTO book);

    @Operation(
            summary = "Update book",
            description = """
                     Updates a book's information by passing in a JSON, XML or YAML
                     representation of the updated book.
                    """,
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(description = "No content", responseCode = "204"),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    BookDTO update(@RequestBody BookDTO book);

    @Operation(
            summary = "Delete book",
            description = "Deletes a specific book by your ID.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Successful operation",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404"),
                    @ApiResponse(description = "Internal server error", responseCode = "500")
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
