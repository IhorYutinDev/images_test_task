package ua.ihor.ImagesTestTask.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
public class ErrorResponse {
    private String error;
    public ErrorResponse(String error) {
        this.error = error;
    }
}
