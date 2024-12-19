package ua.ihor.ImagesTestTask.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ihor.ImagesTestTask.dtos.CreateSlideshowRequest;
import ua.ihor.ImagesTestTask.entities.ErrorResponse;
import ua.ihor.ImagesTestTask.entities.ResponseDTO;
import ua.ihor.ImagesTestTask.exceptions.InvalidQueryParametersException;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.models.ProofOfPlay;
import ua.ihor.ImagesTestTask.models.Slide;
import ua.ihor.ImagesTestTask.models.Slideshow;
import ua.ihor.ImagesTestTask.services.SlideshowService;

import java.util.List;

@RestController
public class SlideshowController {
    private SlideshowService slideshowService;

    @Autowired
    public SlideshowController(SlideshowService slideshowService) {
        this.slideshowService = slideshowService;
    }

    @Operation(summary = "Add a new slideshow", description = "Adds a new slideshow to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Slideshow added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid slideshow data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/addSlideshow")
    public ResponseEntity<Slideshow> addSlideshow(@RequestBody CreateSlideshowRequest request) {
        Slideshow slideshow = slideshowService.createSlideshow(request);
        return ResponseEntity.ok(slideshow);
    }

    @Operation(summary = "Delete slideshow", description = "Delete slideshow to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Slideshow deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Slideshow not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/deleteSlideshow/{id}")
    public ResponseEntity<ResponseDTO> deleteSlideshow(@PathVariable Long id) {
        slideshowService.deleteSlideshow(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(true, "Successfully deleted slideshow with id: " + id));
    }

    @Operation(summary = "Get slideshow", description = "Get slideshow by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Slideshow has founded"),
            @ApiResponse(responseCode = "400", description = "Invalid slideshow data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("slideshow/{id}/slideshowOrder")
    public ResponseEntity<Slideshow> getSlideshowOrder(@PathVariable Long id) {
        return ResponseEntity.ok(slideshowService.getSlideshowOrder(id));
    }

    @Operation(summary = "Proof of play image in slideshow", description = "Record an event when an image is replaced by the next one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proof event has recorded"),
            @ApiResponse(responseCode = "404", description = "Not found slideshow or image"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/slideShow/{id}/proof-of-play/{imageId}")
    public ResponseEntity<ProofOfPlay> recordProofOfPlay(@PathVariable Long id, @PathVariable Long imageId) {
        return ResponseEntity.ok(slideshowService.recordProofOfPlay(id, imageId));
    }
}
