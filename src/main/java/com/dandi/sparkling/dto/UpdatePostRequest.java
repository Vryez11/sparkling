package com.dandi.sparkling.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePostRequest {

    @Nullable
    @Size(max = 100)
    String title;

    @Nullable
    String content;
}
