package ua.ihor.ImagesTestTask.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSlideRequest {
    @JsonProperty("image_id")
    private Long imageId;
    private Integer duration;
}
