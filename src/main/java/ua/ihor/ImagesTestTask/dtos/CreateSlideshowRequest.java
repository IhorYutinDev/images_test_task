package ua.ihor.ImagesTestTask.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateSlideshowRequest {
    private String name;
    private List<AddSlideRequest> slides;
}
