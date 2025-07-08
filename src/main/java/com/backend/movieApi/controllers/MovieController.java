package com.backend.movieApi.controllers;

import com.backend.movieApi.dto.MovieDTO;
import com.backend.movieApi.dto.MoviePageResponse;
import com.backend.movieApi.exceptions.EmptyFileException;
import com.backend.movieApi.service.MovieService;
import com.backend.movieApi.utils.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDTO> addMovieHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String movieDTO) throws IOException {

        if(file.isEmpty()){
            throw new EmptyFileException("File is empty! Please include file.");
        }
        MovieDTO dto = convertToMovieDTO(movieDTO);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Integer movieId){
        return new ResponseEntity<>(movieService.getMovie(movieId), HttpStatus.OK);
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<MovieDTO>> getAllMovies(){
//        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
//    }

    @GetMapping("/all")
    public ResponseEntity<MoviePageResponse> getMoviesPaginated(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/all-Sorted")
    public ResponseEntity<MoviePageResponse> getMoviesPaginatedAndSorted(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ){
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, sortDir));
    }

    @PutMapping("/update-movie/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(@RequestPart MultipartFile file,
                                                @RequestPart String movieDTO,
                                                @PathVariable Integer movieId) throws IOException {
        MovieDTO dto = convertToMovieDTO(movieDTO);
        return new ResponseEntity<>(movieService.updateMovie(movieId, dto, file), HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }


    private MovieDTO convertToMovieDTO(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDTO.class);
    }
}
