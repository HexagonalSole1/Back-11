package org.devquality.privateproyect.core.infrastructure.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private String message;

    private T data;

    private HttpStatus status;

    private Boolean success;


    public ResponseEntity<BaseResponse<T>> toResponseEntity() {
        HttpStatus responseStatus = this.status != null ? this.status : HttpStatus.OK;
        return new ResponseEntity<>(this, responseStatus);
    }
    public ResponseEntity<BaseResponse<T>> success() {
        this.status = HttpStatus.OK;
        return new ResponseEntity<>(this, HttpStatus.OK);
    }

    public ResponseEntity<BaseResponse<T>> error() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(this, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
