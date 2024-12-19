package ua.ihor.ImagesTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.Slide;
import ua.ihor.ImagesTestTask.models.Slideshow;

import java.util.List;

@Repository
public interface SlideshowRepository extends JpaRepository<Slideshow, Long> {
}

