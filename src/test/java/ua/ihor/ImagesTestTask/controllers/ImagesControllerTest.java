package ua.ihor.ImagesTestTask.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.ihor.ImagesTestTask.dtos.ImageDTO;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.services.ImagesService;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImagesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImagesService imageService;

    @InjectMocks
    private ImagesController imagesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imagesController).build();
    }

    @Test
    void testAddImage() throws Exception {
        // Arrange
        ImageDTO dto = new ImageDTO("http://example.com/image.jpg");
        Image image = new Image(dto.getUrl());
        when(imageService.addImage(Mockito.any(Image.class))).thenReturn(image);

        // Act and Assert
        mockMvc.perform(post("/addImage")
                        .contentType("application/json")
                        .content("{\"url\":\"http://example.com/image.jpg\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("http://example.com/image.jpg"));
    }

    @Test
    void testDeleteImage() throws Exception {
        // Arrange
        long imageId = 1L;

        Mockito.doNothing().when(imageService).deleteImage(imageId);

        // Act and Assert
        mockMvc.perform(delete("/deleteImage/{id}", imageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted image with id: " + imageId));
    }

    @Test
    void testSearchSlidesWithValidParams() throws Exception {
        // Arrange
        String keyword = "image";
        Integer duration = 5;
        List<SlideshowImageDuration> images = Arrays.asList(new SlideshowImageDuration(1,"test","http://example.com/image1.jpg", 5));
        when(imageService.searchSlideshowOrder(keyword, duration)).thenReturn(images);

        // Act and Assert
        mockMvc.perform(get("/images/search")
                        .param("keyword", keyword)
                        .param("duration", duration.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].url").value("http://example.com/image1.jpg"));
    }
}
