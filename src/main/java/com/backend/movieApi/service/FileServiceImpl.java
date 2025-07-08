package com.backend.movieApi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path,  MultipartFile file) throws IOException {
        //get name of the file
        String fileName = file.getOriginalFilename();

        //get the file path, where the file will actually be stored
        String filePath = path + File.separator + fileName;

        //create a file object
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }

        //copy the file or upload file to the path
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator +  fileName;
        return new FileInputStream(filePath);
    }
}
