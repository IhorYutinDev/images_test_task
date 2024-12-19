package ua.ihor.ImagesTestTask.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.ihor.ImagesTestTask.dtos.ImageDTO;
import ua.ihor.ImagesTestTask.dtos.SlideshowImageDuration;
import ua.ihor.ImagesTestTask.entities.ResponseDTO;
import ua.ihor.ImagesTestTask.exceptions.InvalidQueryParametersException;
import ua.ihor.ImagesTestTask.models.Image;
import ua.ihor.ImagesTestTask.services.ImagesService;

import java.util.List;


@RestController
public class ImagesController {
    private ImagesService imageService;

    @Autowired
    public ImagesController(ImagesService imageService) {
        this.imageService = imageService;
    }


    @Operation(summary = "Add a new image", description = "Adds a new image to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/addImage")
    public ResponseEntity<Image> addImage(@RequestBody ImageDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.addImage(new Image(dto.getUrl())));
    }

    @Operation(summary = "Delete image", description = "Delete image to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Image not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<ResponseDTO> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(true, "Successfully deleted image with id: " + id));
    }

    @Operation(summary = "Search images", description = "Search image by keyword(url query param) or/and duration(duration query param)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Received images list"),
            @ApiResponse(responseCode = "400", description = "Provided invalid params"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("images/search")
    public ResponseEntity<List<SlideshowImageDuration>> searchSlides(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer duration) {

        if (keyword == null && duration == null) {
            throw new InvalidQueryParametersException("At least one search parameter (keyword or duration) must be provided");
        }

        return ResponseEntity.ok(imageService.searchSlideshowOrder(keyword, duration));
    }
}
