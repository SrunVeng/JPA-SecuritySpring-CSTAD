package com.example.mbaningapijpapractice.features.file.dto;


import lombok.Builder;

@Builder
public record FileResponse(
        String name,
        Long size,
        String extension,
        String contentType,
        String uri
) {
}