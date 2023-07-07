package com.example.pattern.request;

import javax.persistence.Access;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimeRequest {
    Long id;
    String name;
    Float rating;
    String genre;
    boolean isDeleted = false;
//    LocalDateTime createdOn;
//    LocalDateTime updatedOn;
//    Boolean deleted = false;
//    Boolean active = true;
//    Long createdBy;
//    Long updatedBy;
}
