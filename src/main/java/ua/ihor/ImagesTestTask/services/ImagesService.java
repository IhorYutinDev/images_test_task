package ua.ihor.ImagesTestTask.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.exceptions.ImageNotFoundException;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.repositories.ImagesRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;


@Service
public class ImagesService {
    private ImagesRepository imageRepository;

    @Autowired
    public ImagesService(ImagesRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image addImage(Image image) {
        // Validate URL (JPEG, PNG, WEBP)
        if (!isValidImageUrl(image.getUrl())) {
            throw new IllegalArgumentException("Invalid image format");
        }


        try {
            imageRepository.save(image);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("Failed to add image with url: '" + image.getUrl() + "' it is already exists");
        }

        return imageRepository.save(image);
    }

    public void deleteImage(Long id) throws ImageNotFoundException {
        Optional<Image> image = imageRepository.findById(id);
        image.ifPresentOrElse(
                img -> imageRepository.deleteById(id), // Delete if present
                () -> {
                    throw new EntityNotFoundException("Image with id " + id + " does not exist.");
                }
        );
    }

    public List<Image> searchImages(String keyword, int duration) {
        return null;
//        return imageRepository.findByKeywordAndDuration(keyword, duration);
    }

    public boolean isValidImageUrl(String imageUrl) {
        try {
            // Open a connection to the image URL

            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestMethod("HEAD"); // Use HEAD to check the headers without downloading the entire file
            connection.setConnectTimeout(5000); // Set timeout for the connection
            connection.setReadTimeout(5000);

            // Get the content type from the response headers
            String contentType = connection.getContentType();

            // Check if the content type starts with 'image/'
            return contentType != null && contentType.startsWith("image/");
        } catch (IOException e) {
            // Handle error, such as invalid URL or network issues
            return false;
        }
    }

    public List<SlideshowImageDuration> searchSlideshowOrder(String keyword, Integer duration) {
       return imageRepository.findUrlsAndDurations(keyword, duration);
    }
}
