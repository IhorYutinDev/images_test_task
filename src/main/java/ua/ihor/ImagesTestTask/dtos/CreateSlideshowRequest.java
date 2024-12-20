package ua.ihor.ImagesTestTask.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateSlideshowRequest {
    private String name;
    private List<AddSlideRequest> slides;

    public CreateSlideshowRequest() {
    }
}
