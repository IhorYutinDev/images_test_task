package ua.ihor.ImagesTestTask.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SlideshowImageDuration {
    private long id;
    private String name;
    private String url;
    private int duration;
}
