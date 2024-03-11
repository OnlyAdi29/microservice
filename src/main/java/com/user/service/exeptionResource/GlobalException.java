package com.user.service.exeptionResource;

import com.user.service.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ApiResponse> userNotFound(UserNotFound ex) {
       // ApiResponse response = new ApiResponse(HttpStatus.NOT_FOUND,ex.getMessage());
      //  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        ApiResponse response = ApiResponse.builder().success(true).message(ex.getMessage()).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<ApiResponse>(response,HttpStatus.NOT_FOUND);
    }

}
