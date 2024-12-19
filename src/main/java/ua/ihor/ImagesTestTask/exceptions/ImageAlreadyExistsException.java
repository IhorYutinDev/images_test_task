package ua.ihor.ImagesTestTask.exceptions;

public class ImageAlreadyExistsException extends RuntimeException {
    public ImageAlreadyExistsException(String message) {
        super(message);
    }
}
