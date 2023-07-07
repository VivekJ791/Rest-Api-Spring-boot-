package com.example.pattern.service;

import com.example.pattern.exception.ResourceNotFoundException;
import com.example.pattern.model.Anime;
import com.example.pattern.request.AnimeRequest;
import com.example.pattern.response.AnimeResponse;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimeService {
    public AnimeResponse addAnime(AnimeRequest animeRequest) throws ResourceNotFoundException;
    public List<AnimeResponse> getAllAnimes();
    //criteria methods
    public List<AnimeResponse> getAnime();
    public AnimeResponse getAnimeByIdc(Long id);
    public List<AnimeResponse> getByIdOrName(Long id, String name);
    public List<AnimeResponse> getByNameAndRating(String name,Float rating);
    public List<AnimeResponse> getIdGreaterThan(Long id);
    public List<AnimeResponse> getIdGreaterThanEqualTo(Long id);
    public AnimeResponse getAnimeById(long id) throws ResourceNotFoundException;
    public AnimeResponse updateAnime(Long id,AnimeRequest animeRequest) throws ResourceNotFoundException;
    public String softDeleteAnime(Long id) throws ResourceNotFoundException;
    public String deleteAnime(Long id) throws ResourceNotFoundException;
//    public List<Anime> softDeletedAnimes();
    public List<Anime> findByDeletedByTrue();
    public List<Anime> searchAnime(String query);

}
