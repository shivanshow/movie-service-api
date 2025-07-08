package com.backend.movieApi.controllers;

import com.backend.movieApi.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileController {

    @Value("${project.poster}")
    private String path;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPosterHandler(@RequestPart MultipartFile file) throws IOException {
        String uploadedFile = fileService.uploadFile(path, file);
        return ResponseEntity.ok("Poster Uploaded: " + uploadedFile);
    }

    @GetMapping("/{fileName}")
    public void serverFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        try (InputStream inputStream = fileService.getResourceFile(path, fileName)) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(inputStream, response.getOutputStream());
        }
    }
}
