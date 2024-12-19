package ua.ihor.ImagesTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.models.Image;

import java.util.List;


@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {
    @Query("SELECT new ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration(ss.id, ss.name, i.url, s.duration) " +
            "FROM Slide s " +
            "JOIN s.image i " +
            "JOIN s.slideshow ss " +
            "WHERE i.url LIKE CONCAT('%', :keyword, '%') " +
            "OR s.duration = :duration")
    List<SlideshowImageDuration> findUrlsAndDurations(@Param("keyword") String keyword, @Param("duration") Integer duration);
}
