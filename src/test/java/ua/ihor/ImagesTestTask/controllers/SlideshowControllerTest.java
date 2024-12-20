package ua.ihor.ImagesTestTask.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ihor.ImagesTestTask.dtos.CreateSlideshowRequest;
import ua.ihor.ImagesTestTask.entities.ResponseDTO;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.ProofOfPlay;
import ua.ihor.ImagesTestTask.models.Slideshow;
import ua.ihor.ImagesTestTask.services.SlideshowService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SlideshowController.class)
class SlideshowControllerTest {

    @Mock
    private SlideshowService slideshowService;

    @InjectMocks
    private SlideshowController slideshowController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class RatesServiceTestConfig {
        @Bean
        public SlideshowService ratesService() {
            return Mockito.mock(SlideshowService.class);
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(slideshowController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddSlideshow() throws Exception {
        // Arrange
        CreateSlideshowRequest request = new CreateSlideshowRequest("Test Slideshow", null);
        Slideshow slideshow = new Slideshow();
        slideshow.setName("Test Slideshow");
        when(slideshowService.createSlideshow(Mockito.any(CreateSlideshowRequest.class)))
                .thenReturn(slideshow);

        // Act & Assert
        mockMvc.perform(post("/addSlideshow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Slideshow"));
    }

    @Test
    void testDeleteSlideshow() throws Exception {
        // Arrange
        Long slideshowId = 1L;
        ResponseDTO responseDTO = new ResponseDTO(true, "Successfully deleted slideshow with id: " + slideshowId);
        doNothing().when(slideshowService).deleteSlideshow(slideshowId);

        // Act & Assert
        mockMvc.perform(delete("/deleteSlideshow/{id}", slideshowId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted slideshow with id: 1"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetSlideshowOrder() throws Exception {
        // Arrange
        Long slideshowId = 1L;
        Slideshow slideshow = new Slideshow();
        slideshow.setName("Test Slideshow");
        slideshow.setId(slideshowId);
        when(slideshowService.getSlideshowOrder(slideshowId)).thenReturn(slideshow);

        // Act & Assert
        mockMvc.perform(get("/slideshow/{id}/slideshowOrder", slideshowId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Slideshow"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testRecordProofOfPlay() throws Exception {
        // Arrange
        Long slideshowId = 1L;
        Long imageId = 1L;
        ProofOfPlay proofOfPlay = new ProofOfPlay();
        Slideshow slideshow = new Slideshow();
        slideshow.setName("Test Slideshow");
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(new Image());
        proofOfPlay.setEventTimestamp(LocalDateTime.now());
        when(slideshowService.recordProofOfPlay(slideshowId, imageId)).thenReturn(proofOfPlay);

        // Act & Assert
        mockMvc.perform(post("/slideShow/{id}/proof-of-play/{imageId}", slideshowId, imageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slideshow.name").value("Test Slideshow"));
    }
}