package com.app.playerservicejava.exception;

import com.app.playerservicejava.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Handles any unexpected or unhandled exceptions that occur within the application.
     *
     * This method acts as a global fallback to catch exceptions that are not explicitly
     * handled by other exception handlers. It logs the error details and returns a
     * standardized error response with HTTP status 500 (Internal Server Error).
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        String message = "Unexpected error occurred";
        LOGGER.error(message, ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                message);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * Handles the exception when there is type mismatch for Request parameters.
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        LOGGER.warn("Invalid parameter type: {}", ex.getMessage());
        String paramName = ex.getName();
        String message = String.format("Invalid value "+ex.getValue()+" for parameter " + paramName);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.getReasonPhrase(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles the exception when there is any missing-required Request parameters.
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *      *         the error message and HTTP status code 400
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        LOGGER.warn("Missing required parameter: {}", ex.getMessage());
        String paramName = ex.getParameterName();
        String message = String.format("Missing required request parameter: %s", paramName);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles the exception when there is any missing request body in post
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *      *         the error message and HTTP status code 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMissingBody(HttpMessageNotReadableException ex) {
        LOGGER.warn("Request body is either missing or malformed: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request body is required but was missing or malformed."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles the exception for invalid URL endpoint
     @param ex the exception that was thrown during request processing
      * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *      *         the error message and HTTP status code 404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        LOGGER.warn("No endpoint found for the requested URL: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "The requested endpoint does not exist. Check the URL and try again."
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    /**
     * Handles the generic exception when there is some wrong arguments passed.
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        LOGGER.warn("Invalid request ", ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles the exception when there is some database exception.
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 500
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        LOGGER.error("Database error while accessing player data", ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Database error while accessing player data"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handles the exception when the calling underlying ai model throws exception.
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 503
     */
    @ExceptionHandler(AiModelException.class)
    public ResponseEntity<ErrorResponse> handleAiModelException(AiModelException ex) {
        LOGGER.error("Ai service is unavailable. ",ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                "AI service temporarily unavailable. Please try again later."
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    /**
     * Handles the exception when the request params are missing or have invalid values.
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 400
     */
    @ExceptionHandler(InvalidRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameter(InvalidRequestParameterException ex) {
        LOGGER.warn("Invalid request parameter:{}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles the exception when there is no player data found.
     *
     * @param ex the exception that was thrown during request processing
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
     *         the error message and HTTP status code 404
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(PlayerNotFoundException ex) {
        LOGGER.warn("Player not found : {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
