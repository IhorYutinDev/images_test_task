package ua.ihor.ImagesTestTask.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageEvent {
    private String eventType; // ADD or DELETE
    private Long imageId;
    private String imageUrl;
    private LocalDateTime timestamp;
}
