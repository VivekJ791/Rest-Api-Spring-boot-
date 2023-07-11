package com.example.pattern.controller;

import com.example.pattern.exception.ResourceNotFoundException;
import com.example.pattern.request.AnimeRequest;
import com.example.pattern.response.AnimeResponse;
import com.example.pattern.model.Anime;
import com.example.pattern.service.impl.AnimeServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anime")
public class AnimeController {
    @Autowired
    private AnimeServiceImpl animeService;

    @PostMapping
    public ResponseEntity<AnimeResponse> addAnime(@RequestBody AnimeRequest animeRequest)throws ResourceNotFoundException {
        AnimeResponse animeResponse = animeService.addAnime(animeRequest);
        return new ResponseEntity<>(animeResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AnimeResponse>> getAllAnimes() {
        List<AnimeResponse> allAnimes = animeService.getAllAnimes();
        return new ResponseEntity<>(allAnimes, HttpStatus.OK);
    }
    @GetMapping("id")
    public ResponseEntity<AnimeResponse> getAnimeById(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(animeService.getAnimeById(id),HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<Map<String,Object>> allAnimes(
            @RequestParam(defaultValue = "0")int pageNum,
            @RequestParam(defaultValue = "5")int pageSize,
            @RequestParam(defaultValue = "id")String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toString().toUpperCase());
       animeService.allAnimes(pageNum, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(animeService.allAnimes(pageNum, pageSize, sortField, sortDirection),HttpStatus.OK);
//            return ResponseEntity.ok(anime);
    }

    // pagination with sort

    @GetMapping("/criteria")
    public ResponseEntity<List<AnimeResponse>> getAnimes() {
        List<AnimeResponse> animeResponseList = animeService.getAnime();
        return new ResponseEntity<>(animeResponseList, HttpStatus.OK);
    }
    @GetMapping("/criteria/{id}")
    public ResponseEntity<AnimeResponse> getAnimeByIdc(@PathVariable Long id){
        AnimeResponse animeById = animeService.getAnimeByIdc(id);
        return new ResponseEntity<>(animeById,HttpStatus.OK);
    }

    @GetMapping("/criteria/{id}/{name}")
    public ResponseEntity<List<AnimeResponse>> getByIdOrName(@PathVariable Long id,@PathVariable String name){
        List<AnimeResponse> list = animeService.getByIdOrName(id, name);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    @GetMapping("/criteria/greaterThan/{id}")
    public ResponseEntity<List<AnimeResponse>> getIdGreaterThan(@PathVariable Long id){
        List<AnimeResponse> list = animeService.getIdGreaterThan(id);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeResponse> getAnimeById(@PathVariable("id") long id) throws ResourceNotFoundException {
        try {
            return new ResponseEntity<>(animeService.getAnimeById(id), HttpStatus.OK);
        } catch (Exception e) {
           throw new ResourceNotFoundException("anime","id",id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimeResponse> updateAnime(@PathVariable("id") long id, @RequestBody AnimeRequest animeRequest) throws ResourceNotFoundException {
        try{
            AnimeResponse animeResponse = animeService.updateAnime(id, animeRequest);
            return new ResponseEntity<>(animeResponse, HttpStatus.OK);
        }
        catch (Exception e){
         throw new ResourceNotFoundException("anime","id",id);
        }
    }

    @DeleteMapping("/softDelete/{id}")
    public ResponseEntity<String> softDeleteAnime(@PathVariable("id")Long id) throws ResourceNotFoundException{
       return new ResponseEntity<>(animeService.softDeleteAnime(id),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnime(@PathVariable("id") long id) throws ResourceNotFoundException {
        try {
            animeService.deleteAnime(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/softDeleted")
//    public ResponseEntity<List<Anime>> getSoftDeletedAnimes(@RequestBody Anime anime){
//        return new ResponseEntity<>(animeService.softDeletedAnimes(),HttpStatus.OK);
//    }
    @GetMapping("/allSoftDeleted")
    public ResponseEntity<List<Anime>> getSoftDeletedAnimesList(){
        return new ResponseEntity<>(animeService.findByDeletedByTrue(),HttpStatus.OK);
    }
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Anime>> searchAnime(@PathVariable String name){
        return new ResponseEntity<>(animeService.searchAnime(name),HttpStatus.OK);
    }
}