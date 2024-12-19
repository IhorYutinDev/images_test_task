package ua.ihor.ImagesTestTask.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ihor.ImagesTestTask.dtos.AddSlideRequest;
import ua.ihor.ImagesTestTask.dtos.CreateSlideshowRequest;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.ProofOfPlay;
import ua.ihor.ImagesTestTask.models.Slide;
import ua.ihor.ImagesTestTask.models.Slideshow;
import ua.ihor.ImagesTestTask.repositories.ImagesRepository;
import ua.ihor.ImagesTestTask.repositories.ProofOfPlayRepository;
import ua.ihor.ImagesTestTask.repositories.SlideshowRepository;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class SlideshowService {
    private final SlideshowRepository slideshowRepository;
    private final ProofOfPlayRepository proofOfPlayRepository;
    private final ImagesRepository imageRepository;


    @Autowired
    public SlideshowService(SlideshowRepository slideshowRepository, ProofOfPlayRepository proofOfPlayRepository, ImagesRepository imageRepository) {
        this.slideshowRepository = slideshowRepository;
        this.proofOfPlayRepository = proofOfPlayRepository;
        this.imageRepository = imageRepository;
    }


    public Slideshow createSlideshow(CreateSlideshowRequest createSlideshowRequest) {
        Slideshow slideshow = new Slideshow();
        slideshow.setName(createSlideshowRequest.getName());

        List<Slide> slides = new ArrayList<>();
        for (AddSlideRequest slideRequest : createSlideshowRequest.getSlides()) {
            Image image = imageRepository.findById(slideRequest.getImageId())
                    .orElseThrow(() -> new EntityNotFoundException("Image with id: " + slideRequest.getImageId() + " not found"));

            Slide slide = new Slide();
            slide.setSlideshow(slideshow);
            slide.setImage(image);
            slide.setDuration(slideRequest.getDuration());
            slide.setCreated(LocalDateTime.now());
            slides.add(slide);
        }

        slideshow.setSlides(slides);
        return slideshowRepository.save(slideshow);
    }

    public void deleteSlideshow(Long id) {
        slideshowRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Slideshow not found with ID: " + id));

        slideshowRepository.deleteById(id);
    }

//    public List<Slide> getSlideshowOrder(Long slideshowId) {
//        Slideshow slideshow = slideshowRepository.findById(slideshowId)
//                .orElseThrow(() -> new EntityNotFoundException("Not found slideshow with ID: " + slideshowId));
//
//        return slideshow.getSlides();
//    }


    public Slideshow getSlideshowOrder(Long slideshowId) {
        return slideshowRepository.findById(slideshowId)
                .orElseThrow(() -> new EntityNotFoundException("Not found slideshow with ID: " + slideshowId));
    }




    public ProofOfPlay recordProofOfPlay(long slideshowId, long imageId) {
        Slideshow slideshow = slideshowRepository.findById(slideshowId)
                .orElseThrow(() -> new EntityNotFoundException("Slideshow not found"));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        ProofOfPlay proofOfPlay = new ProofOfPlay();
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(image);
        proofOfPlay.setEventTimestamp(LocalDateTime.now());
        return proofOfPlayRepository.save(proofOfPlay);
    }
}
