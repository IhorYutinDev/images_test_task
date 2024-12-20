package ua.ihor.ImagesTestTask.sevices;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.exceptions.ImageAlreadyExistsException;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.repositories.ImagesRepository;
import ua.ihor.ImagesTestTask.services.ImagesService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ImagesServiceTest {
    @Mock
    private ImagesRepository imageRepository;

    @InjectMocks
    private ImagesService imagesService;

    public ImagesServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddImage_validImageUrl_success() {
        // Arrange
        ImagesService spyImagesService = spy(imagesService);

        String validUrl = "http://test.url/image.jpg";
        Image image = new Image();
        image.setUrl(validUrl);

        doReturn(true).when(spyImagesService).isValidImageUrl(validUrl);

        when(imageRepository.save(any(Image.class))).thenReturn(image);

        // Act
        Image result = spyImagesService.addImage(image);

        // Assert
        assertNotNull(result);
        assertEquals(validUrl, result.getUrl());
    }

    @Test
    public void testAddImage_invalidImageUrl_throwsException() {
        // Arrange
        String invalidUrl = "http://test.url/not-an-image.txt";
        Image image = new Image();
        image.setUrl(invalidUrl);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> imagesService.addImage(image));
        assertEquals("Invalid image format", exception.getMessage());
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    public void testAddImage_duplicateImage_throwsException() {
        // Arrange
        ImagesService spyImagesService = spy(imagesService);
        String validUrl = "http://test.url/image.jpg";
        Image image = new Image();
        image.setUrl(validUrl);

        doReturn(true).when(spyImagesService).isValidImageUrl(validUrl);
        when(imageRepository.save(any(Image.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // Act & Assert
        ImageAlreadyExistsException exception = assertThrows(ImageAlreadyExistsException.class, () -> spyImagesService.addImage(image));
        assertEquals("Failed to add image with url: 'http://test.url/image.jpg' it is already exists", exception.getMessage());
    }

    @Test
    public void testDeleteImage_existingImage_success() {
        // Arrange
        Long imageId = 1L;
        Image image = new Image();
        image.setId(imageId);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));

        // Act
        imagesService.deleteImage(imageId);

        // Assert
        verify(imageRepository, times(1)).deleteById(imageId);
    }

    @Test
    public void testDeleteImage_nonExistentImage_throwsException() {
        // Arrange
        Long imageId = 1L;

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> imagesService.deleteImage(imageId));
        assertEquals("Image with id 1 does not exist.", exception.getMessage());
        verify(imageRepository, never()).deleteById(imageId);
    }


    @Test
    public void testSearchSlideshowOrder_resultsFound_success() {
        // Arrange
        String keyword = "test";
        Integer duration = 120;

        SlideshowImageDuration result1 = new SlideshowImageDuration(1, "test1", "http://test.url/image1.jpg", 120);
        SlideshowImageDuration result2 = new SlideshowImageDuration(2, "test2", "http://test.url/image2.jpg", 150);

        when(imageRepository.findUrlsAndDurations(keyword, duration))
                .thenReturn(Arrays.asList(result1, result2));

        // Act
        List<SlideshowImageDuration> results = imagesService.searchSlideshowOrder(keyword, duration);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("http://test.url/image1.jpg", results.get(0).getUrl());
        assertEquals(120, results.get(0).getDuration());
    }

    @Test
    public void testSearchSlideshowOrder_noResults_returnsEmptyList() {
        // Arrange
        String keyword = "nonexistent";
        Integer duration = 100;

        when(imageRepository.findUrlsAndDurations(keyword, duration)).thenReturn(Collections.emptyList());

        // Act
        List<SlideshowImageDuration> results = imagesService.searchSlideshowOrder(keyword, duration);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}