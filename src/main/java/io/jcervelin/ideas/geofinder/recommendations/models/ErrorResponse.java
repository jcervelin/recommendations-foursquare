package io.jcervelin.ideas.geofinder.recommendations.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 7586552866199651913L;

    private Integer code;
    private HttpStatus status;
    private String message;
}