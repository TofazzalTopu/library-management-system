package com.library.management.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "201",
                description = "Successfully Created",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                description = "Unauthorized",
                responseCode = "401",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(mediaType = "application/json")
        )
})
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface APIDocumentation {
}
