package ua.ihor.ImagesTestTask.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.events.ImageEvent;
import ua.ihor.ImagesTestTask.exceptions.ImageAlreadyExistsException;
import ua.ihor.ImagesTestTask.exceptions.ImageNotFoundException;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.repositories.ImagesRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for image operations
 */
@Service
public class ImagesService {
    private ImagesRepository imageRepository;
    private final KafkaTemplate<String, ImageEvent> kafkaTemplate;

    @Autowired
    public ImagesService(ImagesRepository imageRepository, KafkaTemplate<String, ImageEvent> kafkaTemplate) {
        this.imageRepository = imageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Adds a new image to the database
     * 
     * @param image the image to be added
     * @return the added image
     */
    public Image addImage(Image image) {
        // Validate URL (JPEG, PNG, WEBP)
        if (!isValidImageUrl(image.getUrl())) {
            throw new IllegalArgumentException("Invalid image format");
        }

        try {
            Image savedImage = imageRepository.save(image);
            // Send Kafka event for image addition
            ImageEvent event = new ImageEvent("ADD", savedImage.getId(), savedImage.getUrl(), LocalDateTime.now());
            kafkaTemplate.send("image-events", event);
            return savedImage;
        } catch (DataIntegrityViolationException e) {
            throw new ImageAlreadyExistsException("Failed to add image with url: '" + image.getUrl() + "' it is already exists");
        }
    }

    /**
     * Deletes an image from the database
     * 
     * @param id the id of the image to be deleted
     */
    public void deleteImage(Long id) throws ImageNotFoundException {
        Optional<Image> image = imageRepository.findById(id);
        image.ifPresentOrElse(
                img -> {
                    imageRepository.deleteById(id);
                    // Send Kafka event for image deletion
                    ImageEvent event = new ImageEvent("DELETE", img.getId(), img.getUrl(), LocalDateTime.now());
                    kafkaTemplate.send("image-events", event);
                },
                () -> {
                    throw new EntityNotFoundException("Image with id " + id + " does not exist.");
                }
        );
    }

    /**
     * Checks if the given image URL is valid
     * 
     * @param imageUrl the URL of the image to be checked
     * @return true if the image URL is valid, false otherwise
     */
    public boolean isValidImageUrl(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            String contentType = connection.getContentType();
            return contentType != null && contentType.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Searches for images in the slideshow order
     * 
     * @param keyword the keyword to search for
     * @param duration the duration of the slideshow
     * @return a list of slideshow image durations
     */
    public List<SlideshowImageDuration> searchSlideshowOrder(String keyword, Integer duration) {
        return imageRepository.findUrlsAndDurations(keyword, duration);
    }
}
