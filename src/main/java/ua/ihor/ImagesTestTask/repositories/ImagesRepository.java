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
    @Query("""
            select new ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration(
                i.id, 
                i.url, 
                ss.name, 
                s.duration
            )
            from Image i
            left join Slide s on i.id = s.image.id
            left join s.slideshow ss
            where i.url like CONCAT('%', :keyword, '%') or s.duration = :duration
            """)
    List<SlideshowImageDuration> findUrlsAndDurations(@Param("keyword") String keyword, @Param("duration") Integer duration);
}
