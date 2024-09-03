package com.example.mbaningapijpapractice.mapper;


import com.example.mbaningapijpapractice.domain.File;
import com.example.mbaningapijpapractice.features.file.dto.FileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    File fromFileResponse(FileResponse fileResponse);
    FileResponse toFileResponse(File file);


}
