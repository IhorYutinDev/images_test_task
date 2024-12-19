package ua.ihor.ImagesTestTask.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "slideshows")
public class Slideshow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "slideshow", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("created ASC")
    private List<Slide> slides = new ArrayList<>();

    public Slideshow() {
    }
}
