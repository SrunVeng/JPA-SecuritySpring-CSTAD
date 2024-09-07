package com.example.mbaningapijpapractice.features.file;

import com.example.mbaningapijpapractice.Util.FileUtil;
import com.example.mbaningapijpapractice.domain.File;
import com.example.mbaningapijpapractice.features.file.dto.FileResponse;

import com.example.mbaningapijpapractice.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Value("${file-server.server-path}")
    private String serverPath;

    @Value("${file-server.base-uri}")
    private String baseUri;

    @Override
    public List<FileResponse> upload(List<MultipartFile> files) throws IOException {
        List<FileResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            responses.add(upload(file));

        }
        return responses;
    }

    @Override
    public FileResponse getFileByName(String Name) {
        File file = fileRepository
                .findByname(Name).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not Found"));
        return fileMapper.toFileResponse(file);
    }

    @Override
    public FileResponse getFileById(Integer id) {
        File file = fileRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "File Not Found"));

        return fileMapper.toFileResponse(file);
    }

    public FileResponse upload(MultipartFile file) throws IOException {

        String newName = FileUtil.generateFileName(file.getOriginalFilename());
        String extension = FileUtil.extractExtension(Objects.requireNonNull(file.getOriginalFilename()));

        Path path = Path.of(serverPath + newName);
        Files.copy(file.getInputStream(), path);


        FileResponse response = FileResponse.builder()
                .name(newName)
                .size(file.getSize())
                .extension(extension)
                .contentType(file.getContentType())
                .uri(baseUri + newName)
                .build();

        File saved = fileMapper.fromFileResponse(response);
        fileRepository.save(saved);

        return response;

    }
}
