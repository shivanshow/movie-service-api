package com.backend.movieApi.service;

import com.backend.movieApi.dto.MovieDTO;
import com.backend.movieApi.dto.MoviePageResponse;
import com.backend.movieApi.entities.Movie;
import com.backend.movieApi.exceptions.FileExistsException;
import com.backend.movieApi.exceptions.MovieNotFoundException;
import com.backend.movieApi.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseURL;

    private final MovieRepository movieRepository;
    private final FileService fileService;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException {

        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileExistsException("File already exists! Please enter another fileName.");
        }

        String uploadedFileName = fileService.uploadFile(path, file);

        movieDTO.setPoster(uploadedFileName);

        Movie movie = new Movie(
                null,
                movieDTO.getTitle(),
                movieDTO.getDirector(),
                movieDTO.getStudio(),
                movieDTO.getMovieCast(),
                movieDTO.getReleaseYear(),
                movieDTO.getPoster()
        );

        Movie savedMovie = movieRepository.save(movie);

        String posterURL = baseURL + "/file/" + savedMovie.getPoster();

        MovieDTO response = new MovieDTO(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterURL
        );

        return response;
    }

    @Override
    public MovieDTO getMovie(Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        String posterURL = baseURL + "/file/" + movie.getPoster();

        MovieDTO response = new MovieDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterURL
        );
        return response;
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        List<MovieDTO> movieDTOS = new ArrayList<>();

        for(Movie movie : movies){
            String posterURL = baseURL + "/file/" + movie.getPoster();
            MovieDTO response = new MovieDTO(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterURL
            );
            movieDTOS.add(response);
        }
        return movieDTOS;
    }

    @Override
    public MovieDTO updateMovie(Integer movieId, MovieDTO movieDTO, MultipartFile file) throws IOException {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        String fileName = existingMovie.getPoster();
        if(file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }
        movieDTO.setPoster(fileName);

        Movie movie = new Movie(
                existingMovie.getMovieId(),
                movieDTO.getTitle(),
                movieDTO.getDirector(),
                movieDTO.getStudio(),
                movieDTO.getMovieCast(),
                movieDTO.getReleaseYear(),
                movieDTO.getPoster()
        );

        Movie updatedMovie = movieRepository.save(movie);

        String posterURL = baseURL + "/file/" + updatedMovie.getPoster();

        MovieDTO response = new MovieDTO(
                updatedMovie.getMovieId(),
                updatedMovie.getTitle(),
                updatedMovie.getDirector(),
                updatedMovie.getStudio(),
                updatedMovie.getMovieCast(),
                updatedMovie.getReleaseYear(),
                updatedMovie.getPoster(),
                posterURL
        );
        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        String fileName = existingMovie.getPoster();
        Files.delete(Paths.get(path + File.separator + fileName));

        movieRepository.deleteById(movieId);
        return "Movie deleted with id: " + movieId;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDTO> movieDTOS = new ArrayList<>();

        for(Movie movie : movies){
            String posterURL = baseURL + "/file/" + movie.getPoster();
            MovieDTO response = new MovieDTO(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterURL
            );
            movieDTOS.add(response);
        }
        return new MoviePageResponse(movieDTOS, pageSize, pageNumber,
                                     moviePages.getTotalElements(), moviePages.getTotalPages(),
                                    moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                                : Sort.by(dir).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePages = movieRepository.findAll(pageable);

        List<Movie> movies = moviePages.getContent();

        List<MovieDTO> movieDTOS = new ArrayList<>();

        for(Movie movie : movies){
            String posterURL = baseURL + "/file/" + movie.getPoster();
            MovieDTO response = new MovieDTO(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterURL
            );
            movieDTOS.add(response);
        }
        return new MoviePageResponse(movieDTOS, pageSize, pageNumber,
                moviePages.getTotalElements(), moviePages.getTotalPages(),
                moviePages.isLast());
    }
}
