package ua.ihor.ImagesTestTask.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.ProofOfPlay;

import ua.ihor.ImagesTestTask.models.Slideshow;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Testcontainers
@DataJpaTest
public class ProofOfPlayRepositoryTest {
    @Autowired
    private SlideshowRepository slideshowRepository;
    @Autowired
    private ProofOfPlayRepository proofOfPlayRepository;
    @Autowired
    private ImagesRepository imagesRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");


    @Test
    public void checkConnectionTest() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }


    @Test
    public void saveSlideshow_shouldRecordSlideshowImageTest() {
        // given
        long slideshowId = 1;
        long imageId = 1;

        Slideshow slideshow = slideshowRepository.findById(slideshowId).get();
        Image image = imagesRepository.findById(imageId).get();

        ProofOfPlay proofOfPlay = new ProofOfPlay();
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(image);
        proofOfPlay.setEventTimestamp(LocalDateTime.now());
        // when
        proofOfPlayRepository.save(proofOfPlay);
        // then
        assertThat(proofOfPlay.getId()).isEqualTo(1);
        assertThat(proofOfPlay.getImage().getId()).isEqualTo(1);
        assertThat(proofOfPlay.getImage().getUrl()).isEqualTo("http://test.url");
        assertThat(proofOfPlay.getSlideshow().getId()).isEqualTo(1);
        assertThat(proofOfPlay.getSlideshow().getName()).isEqualTo("test_slideshow");
    }
}
