package com.example.pattern.repo;

import com.example.pattern.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepo extends JpaRepository<Anime,Long> {
    @Query(value = "select a from Anime a where a.isDeleted= true")  //native query
    public List<Anime> findByDeletedByTrue();

    @Query(value = "select a from Anime a where a.name like concat('%',:q,'%')")
    public List<Anime> searchAnimeByName(@Param("q")String query);
//    @Query(value = "select a from Anime a where isDeleted=:q")   //jpql query
//    public List<Anime> findByDeleted(@Param("q")String query);
//    List<Anime> getAnimeByDeleted(boolean deleted);
}
