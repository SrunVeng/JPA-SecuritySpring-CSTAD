package com.example.mbaningapijpapractice.features.file;


import com.example.mbaningapijpapractice.features.file.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public List<FileResponse> upload(@RequestPart List<MultipartFile> files) throws IOException {
        return fileService.upload(files);
    }

    @GetMapping("/findByName/{name}")
    public FileResponse getFile(@PathVariable String name)  {
        return fileService.getFileByName(name);
    }

    @GetMapping("/findById/{id}")
    public FileResponse getFile(@PathVariable Integer id)  {
        return fileService.getFileById(id);
    }

}
