package com.dandi.sparkling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostRequest {

    @NotBlank
    @Size(max = 100)
    String title;

    @NotBlank
    String content;
}
