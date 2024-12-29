package ua.ihor.ImagesTestTask.repositories;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.models.Image;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Testcontainers
@DataJpaTest
public class ImagerRepositoryTest {
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
    public void findAllTest() {
        //given
        String url = "http://test.url";
        //when
        List<Image> assets = imagesRepository.findAll();
        //then
        assertThat(assets).isNotNull();
        assertThat(assets.size()).isEqualTo(1);
        assertThat(assets.get(0).getUrl()).isEqualTo(url);
    }


    @Test
    public void deleteByIdTest() {
        //given
        assertThat(imagesRepository.findAll().size()).isEqualTo(1);
        // when
        imagesRepository.deleteById(1L);
        // then
        assertThat(imagesRepository.findAll().size()).isEqualTo(0);
    }


    @Test
    public void saveTest() {
        // given
        String url = "http://test2.url";
        Image image = new Image(url);

        // when
        imagesRepository.save(image);

        // then
        Image findImage = imagesRepository.findById(image.getId()).orElse(null);

        assertThat(findImage).isNotNull();

        assertThat(findImage.getUrl()).isEqualTo(url);
        assertThat(findImage.getId()).isEqualTo(2);
    }

    @Test
    public void checkConstraintSaveTest() {
        // given
        String url = "http://test.url";
        Image image = new Image(url);

        // when
        assertThatThrownBy(() -> imagesRepository.save(image))
                // then
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void getByKeywordTest_shouldNotFound() {
        // given
        String key = "not_match";//http://test.url
        // when
        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(key, null);
        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void getByKeywordTest_shouldFoundOne() {
        // given
        String key = "est.u";//http://test.url
        // when
        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(key, null);
        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void getByDurationTest_shouldFoundOne() {
        // given
        int duration = 120;
        // when
        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(null,  duration);
        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void getByDurationTest_shouldNotFound() {
        // given
        int duration = -1;//120
        // when
        List<SlideshowImageDuration> list = imagesRepository.findUrlsAndDurations(null,  duration);
        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(0);
    }
}
