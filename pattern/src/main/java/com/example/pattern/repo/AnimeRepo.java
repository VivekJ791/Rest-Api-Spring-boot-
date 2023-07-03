package com.example.pattern.repo;

import com.example.pattern.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepo extends JpaRepository<Anime,Long> {
}
