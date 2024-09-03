package com.example.mbaningapijpapractice.features.file;

import com.example.mbaningapijpapractice.features.file.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileResponse> upload(List<MultipartFile >files) throws IOException;

    FileResponse getFileByName(String Name);

    FileResponse getFileById(Integer id);

}
