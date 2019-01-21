package io.jcervelin.ideas.geofinder.recommendations.gateways.https.advices;

import io.jcervelin.ideas.geofinder.recommendations.exceptions.NoDataFoundException;
import io.jcervelin.ideas.geofinder.recommendations.exceptions.TechnicalFaultException;
import io.jcervelin.ideas.geofinder.recommendations.models.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final String ERROR_NOT_DEFINITION = "Unidentified error";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        return createMessage(exception, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponse> noContentFound(final Exception exception) {
        return createMessage(exception, BAD_REQUEST);
    }

    @ExceptionHandler(TechnicalFaultException.class)
    public ResponseEntity<ErrorResponse> offerErrorException(final Exception exception) {
        return createMessage(exception, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createMessage(final Exception exception, final HttpStatus httpStatus) {
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(httpStatus);
        errorResponse.setCode(httpStatus.value());
        errorResponse.setMessage(exception.getMessage()!= null && exception.getMessage().isEmpty()
                ? ERROR_NOT_DEFINITION : exception.getMessage());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}