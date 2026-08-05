package com.dandi.sparkling.post.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRequest {

    @NotBlank
    @Size(max = 100)
    String title;

    @NotBlank
    String content;
}
