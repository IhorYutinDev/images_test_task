package ua.ihor.ImagesTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ihor.ImagesTestTask.models.Slideshow;



@Repository
public interface SlideshowRepository extends JpaRepository<Slideshow, Long> {
}

