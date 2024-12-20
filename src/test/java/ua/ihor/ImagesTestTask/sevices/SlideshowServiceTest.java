package ua.ihor.ImagesTestTask.sevices;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ua.ihor.ImagesTestTask.dtos.AddSlideRequest;
import ua.ihor.ImagesTestTask.dtos.CreateSlideshowRequest;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.ProofOfPlay;
import ua.ihor.ImagesTestTask.models.Slide;
import ua.ihor.ImagesTestTask.models.Slideshow;
import ua.ihor.ImagesTestTask.repositories.ImagesRepository;
import ua.ihor.ImagesTestTask.repositories.ProofOfPlayRepository;
import ua.ihor.ImagesTestTask.repositories.SlideshowRepository;
import ua.ihor.ImagesTestTask.services.SlideshowService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;


public class SlideshowServiceTest {
    @Mock
    private SlideshowRepository slideshowRepository;

    @Mock
    private ProofOfPlayRepository proofOfPlayRepository;

    @Mock
    private ImagesRepository imagesRepository;

    @InjectMocks
    private SlideshowService slideshowService;

    public SlideshowServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateSlideshow() {
        // Arrange
        Image mockImage = new Image();
        mockImage.setId(1L);
        mockImage.setUrl("Test Image");

        when(imagesRepository.findById(1L)).thenReturn(Optional.of(mockImage));

        CreateSlideshowRequest createSlideshowRequest = new CreateSlideshowRequest();
        createSlideshowRequest.setName("Test Slideshow");

        AddSlideRequest slideRequest = new AddSlideRequest();
        slideRequest.setImageId(1L); // Ensure this matches the mocked image ID
        slideRequest.setDuration(5); // Duration set for the slide
        createSlideshowRequest.setSlides(Collections.singletonList(slideRequest));

        Slideshow mockSlideshow = new Slideshow();
        mockSlideshow.setName("Test Slideshow");

        when(slideshowRepository.save(any(Slideshow.class))).thenReturn(mockSlideshow);

        //Act
        Slideshow createdSlideshow = slideshowService.createSlideshow(createSlideshowRequest);
        //Assert
        assertNotNull(createdSlideshow);
        assertEquals("Test Slideshow", createdSlideshow.getName());
    }

    @Test
    public void testCreateSlideshow_imageNotFound() {
        // Arrange
        CreateSlideshowRequest request = new CreateSlideshowRequest();
        request.setName("Test Slideshow");

        AddSlideRequest slideRequest = new AddSlideRequest();
        slideRequest.setImageId(1L);
        slideRequest.setDuration(10);
        request.setSlides(Collections.singletonList(slideRequest));

        when(imagesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> slideshowService.createSlideshow(request));
        assertEquals("Image with id: 1 not found", exception.getMessage());
        verify(imagesRepository, times(1)).findById(1L);
        verify(slideshowRepository, never()).save(any(Slideshow.class));
    }

    @Test
    public void testRecordProofOfPlay_success() {
        // Arrange
        Slideshow mockSlideshow = new Slideshow();
        mockSlideshow.setId(1L);

        Image mockImage = new Image();
        mockImage.setId(1L);

        ProofOfPlay mockProofOfPlay = new ProofOfPlay();
        mockProofOfPlay.setSlideshow(mockSlideshow);
        mockProofOfPlay.setImage(mockImage);
        mockProofOfPlay.setEventTimestamp(LocalDateTime.now());

        when(slideshowRepository.findById(1L)).thenReturn(Optional.of(mockSlideshow));
        when(imagesRepository.findById(1L)).thenReturn(Optional.of(mockImage));
        when(proofOfPlayRepository.save(any(ProofOfPlay.class))).thenReturn(mockProofOfPlay);

        // Act
        ProofOfPlay result = slideshowService.recordProofOfPlay(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(mockSlideshow, result.getSlideshow());
        assertEquals(mockImage, result.getImage());
        verify(slideshowRepository, times(1)).findById(1L);
        verify(imagesRepository, times(1)).findById(1L);
        verify(proofOfPlayRepository, times(1)).save(any(ProofOfPlay.class));
    }

    @Test
    public void testRecordProofOfPlay_slideshowNotFound() {
        // Arrange
        when(slideshowRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> slideshowService.recordProofOfPlay(1L, 1L));
        assertEquals("Slideshow not found", exception.getMessage());
        verify(slideshowRepository, times(1)).findById(1L);
        verify(imagesRepository, never()).findById(anyLong());
        verify(proofOfPlayRepository, never()).save(any(ProofOfPlay.class));
    }
}
