package ua.ihor.ImagesTestTask.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Entity
@Table(name = "slides")
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "slideshow_id", nullable = false)
    private Slideshow slideshow;

//    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @NotNull
    private Integer duration;

    @NotNull
    @Column(name = "created_timestamp")
    private LocalDateTime created;
}
