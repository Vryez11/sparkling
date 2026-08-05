package com.dandi.sparkling.post.update;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRequest {

    @Nullable
    @Size(max = 100)
    String title;

    @Nullable
    String content;
}
