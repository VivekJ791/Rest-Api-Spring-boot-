package com.example.pattern.helper;

import com.example.pattern.model.Anime;
import com.example.pattern.request.AnimeRequest;
import com.example.pattern.response.AnimeResponse;
import org.springframework.stereotype.Component;

@Component
public class AnimeHelper {
    // Get Anime from animeRequest
    public static Anime animeFromAnimeRequest(AnimeRequest animeRequest){
        Anime anime= new Anime(animeRequest.getId(),
                animeRequest.getName(),animeRequest.getRating(),animeRequest.getGenre(),animeRequest.isDeleted());
        return anime;
    }
    // Get AnimeRequest from Anime
    public static AnimeResponse animeResponseFromAnime(Anime anime){
        AnimeResponse animeResponse= new AnimeResponse(anime.getId(),
                anime.getName(),anime.getRating(),anime.getGenre(),anime.isDeleted(),anime.getCreatedOn(),anime.getUpdatedOn(),anime.getDeleted(),anime.getActive(),anime.getCreatedBy(),anime.getUpdatedBy());
        return animeResponse;
    }
}
