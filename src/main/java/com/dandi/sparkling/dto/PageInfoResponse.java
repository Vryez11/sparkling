package com.dandi.sparkling.dto;

import lombok.Getter;

@Getter
public class PageInfoResponse {

    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;
    private final boolean hasNext;

    private PageInfoResponse(int page, int size, long totalCount, int totalPages, boolean hasNext) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
    }

    public static PageInfoResponse from(int page, int size, long totalCount, int totalPages, boolean hasNext) {
        return new PageInfoResponse(page, size, totalCount, totalPages, hasNext);
    }
}
