package com.example.pattern.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimeResponse {
    private Long id;
    private String name;
    private Float rating;
    private String genre;
    private boolean isDeleted = false;
//    LocalDateTime createdOn;
//    LocalDateTime updatedOn;
//    Boolean deleted = false;
//    Boolean active = true;
//    Long createdBy;
//    Long updatedBy;
}
