package ua.ihor.ImagesTestTask.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.exceptions.ImageAlreadyExistsException;
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
            throw new ImageAlreadyExistsException("Failed to add image with url: '" + image.getUrl() + "' it is already exists");
        }

        return imageRepository.save(image);
    }

    public void deleteImage(Long id) throws ImageNotFoundException {
        Optional<Image> image = imageRepository.findById(id);
        image.ifPresentOrElse(
                img -> imageRepository.deleteById(id),
                () -> {
                    throw new EntityNotFoundException("Image with id " + id + " does not exist.");
                }
        );
    }

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

    public List<SlideshowImageDuration> searchSlideshowOrder(String keyword, Integer duration) {
        return imageRepository.findUrlsAndDurations(keyword, duration);
    }
}
