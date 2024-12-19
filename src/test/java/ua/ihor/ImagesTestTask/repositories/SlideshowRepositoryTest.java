package ua.ihor.ImagesTestTask.repositories;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.ihor.ImagesTestTask.dtos.AddSlideRequest;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.Slide;
import ua.ihor.ImagesTestTask.models.Slideshow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@Testcontainers
@DataJpaTest
public class SlideshowRepositoryTest {
    @Autowired
    private SlideshowRepository slideshowRepository;
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
    public void saveSlideshow_shouldReturnSlideshowTest() {
        // given
        String name = "test";
        Slideshow slideshow = new Slideshow();
        slideshow.setName(name);

        List<Slide> slides = new ArrayList<>();

        Slide slide = new Slide();
        slide.setSlideshow(slideshow);

        Image image = imagesRepository.findById(1L).get();
        slide.setImage(image);
        slide.setDuration(140);
        slide.setCreated(LocalDateTime.now());
        slides.add(slide);

        slideshow.setSlides(slides);
        // when
        Slideshow saved = slideshowRepository.save(slideshow);
        // then
        assertThat(saved.getId()).isEqualTo(2);
        assertThat(saved.getName()).isEqualTo(name);
        assertThat(saved.getSlides().get(0).getDuration()).isEqualTo(140);
        assertThat(saved.getSlides().get(0).getImage().getUrl()).isEqualTo(image.getUrl());
    }

    @Test
    public void getSlideshow_shouldReturnSlideshowTest() {
        // given
        long id = 1;
        // when
        Optional<Slideshow> optional = slideshowRepository.findById(id);
        assertThat(optional.isPresent()).isTrue();
        Slideshow slideshow = optional.get();
        // then
        assertThat(slideshow.getId()).isEqualTo(id);
        assertThat(slideshow.getName()).isEqualTo("test_slideshow");
        assertThat(slideshow.getSlides().get(0).getDuration()).isEqualTo(120);
        assertThat(slideshow.getSlides().get(0).getImage().getUrl()).isEqualTo("http://test.url");
    }

    @Test
    public void deleteSlideshowById_shouldDeleteSlideshowTest() {
        // given
        long id = 1L;
        // when
        imagesRepository.deleteById(id);
        //then
        assertThat(slideshowRepository.findById(id).isPresent()).isTrue();
    }
//
//    @Test
//    public void getByKeywordTest_shouldFoundOne() {
//        // given
//        String key = "est.u";//http://test.url
//        // when
//        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(key, null);
//        //then
//        assertThat(list).isNotNull();
//        assertThat(list.size()).isEqualTo(1);
//    }
//
//    @Test
//    public void getByDurationTest_shouldFoundOne() {
//        // given
//        int duration = 120;
//        // when
//        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(null, duration);
//        //then
//        assertThat(list).isNotNull();
//        assertThat(list.size()).isEqualTo(1);
//    }
//
//    @Test
//    public void getByDurationTest_shouldNotFound() {
//        // given
//        int duration = -1;//120
//        // when
//        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(null, duration);
//        //then
//        assertThat(list).isNotNull();
//        assertThat(list.size()).isEqualTo(0);
//    }
}

