package com.backend.movieApi.dto;

import java.util.List;

public record MoviePageResponse(List<MovieDTO> movieDTOS,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {
}
