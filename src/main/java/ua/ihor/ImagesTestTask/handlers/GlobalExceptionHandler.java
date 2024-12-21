package ua.ihor.ImagesTestTask.handlers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ua.ihor.ImagesTestTask.dtos.ResponseDTO;
import ua.ihor.ImagesTestTask.exceptions.ImageAlreadyExistsException;
import ua.ihor.ImagesTestTask.exceptions.InvalidQueryParametersException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleImageNotValidException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ResponseDTO(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ResponseDTO(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageAlreadyExistsException.class)
    public ResponseEntity<ResponseDTO> handleImageAlreadyExistsException(ImageAlreadyExistsException ex) {
        return new ResponseEntity<>(new ResponseDTO(false, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidQueryParametersException.class)
    public ResponseEntity<ResponseDTO> handleImageAlreadyExistsException(InvalidQueryParametersException ex) {
        return new ResponseEntity<>(new ResponseDTO(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
