package com.example.pattern.model;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@FieldDefaults(level= AccessLevel.PRIVATE)
public class BaseEntity {

//    @Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	Long id;

    @Column (name="created_on")
    @Temporal (TemporalType.TIMESTAMP)
    LocalDateTime createdOn;

    @Column (name="updated_on")
    @Temporal(TemporalType.TIMESTAMP)    //by default temporal type= timestamp
    LocalDateTime updatedOn;

    @Column (name="deleted")
    @ColumnDefault(value = "0")
    Boolean deleted = false;

    @Column (name="active")
    @ColumnDefault(value = "1")
    Boolean active = true;

    @Column (name="created_by")
    Long createdBy;

    @Column (name="updated_by")
    Long updatedBy;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdOn = now;
        updatedOn = now;
    }
    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }
}

