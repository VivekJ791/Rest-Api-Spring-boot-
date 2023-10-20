package com.example.pattern.service.impl;

import com.example.pattern.exception.ResourceNotFoundException;
import com.example.pattern.helper.AnimeHelper;
import com.example.pattern.model.Anime;
import com.example.pattern.repo.AnimeRepo;
import com.example.pattern.request.AnimeRequest;
import com.example.pattern.response.AnimeResponse;
import com.example.pattern.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnimeServiceImpl implements AnimeService {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AnimeRepo animeRepo;

    @Override
    public AnimeResponse addAnime(AnimeRequest animeRequest) throws ResourceNotFoundException {

        Anime anime = new Anime();
        if (animeRequest.getId() != null) {
            anime = this.animeRepo.findById(animeRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("anime", "id", animeRequest.getId()));
//            anime.setUpdatedBy(animeRequest.getUpdatedBy());
//            anime.setCreatedOn(anime.getCreatedOn());
        }
        anime.setName(animeRequest.getName());
        anime.setGenre(animeRequest.getGenre());
        anime.setRating(animeRequest.getRating());
//        anime.setCreatedOn(animeRequest.getCreatedOn());
//        anime.setUpdatedOn(animeRequest.getUpdatedOn());
//        anime.setDeleted(animeRequest.getDeleted());
        //       anime.setActive(animeRequest.getActive());
        //     anime.setCreatedBy(animeRequest.getCreatedBy());

        animeRepo.save(anime);

        System.out.println("revert on this ");
        return AnimeHelper.animeResponseFromAnime(anime);
    }

    @Override
    public List<AnimeResponse> getAllAnimes() {
        int pageNum = 5;
        int pageSize = 0;
        List<AnimeResponse> animeResponses = new ArrayList<>();
        PageRequest page = PageRequest.of(pageNum, pageSize);
        Page<Anime> pages = animeRepo.findAll(page);
        List<Anime> content = pages.getContent();
        return content.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList());
/*        for (Anime a:content) {
            animeResponses.add(AnimeHelper.animeResponseFromAnime(a));
        }
        List<Anime> all = animeRepo.findAll();
        for (Anime a: all) {
            animeResponses.add(AnimeHelper.animeResponseFromAnime(a));
        }
        return animeResponses;*/
    }

    @Override
    public AnimeResponse getAnimeById(Long id) throws ResourceNotFoundException {
        Anime anime = animeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("anime", "id", id));
        return AnimeHelper.animeResponseFromAnime(anime);
    }
// Another example of pagintion in spring boot rest api

    public Map<String,Object> allAnimes(int pageNum, int pageSize, String sortField, Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortField);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Anime> list=animeRepo.findAll(pageable);
        List<Anime> content = list.getContent();
        Map<String,Object> hashmap= new HashMap<String,Object>();
        hashmap.put("animes:",content);
        hashmap.put("Total Elements:",list.getTotalElements());
        hashmap.put("Total Pages:",list.getTotalPages());
        hashmap.put("Number:",list.getNumber());
        return hashmap;
//        return animeRepo.findAll(pageable);
    }
    //criteria builders
    @Transactional
    @Override
    public List<AnimeResponse> getAnime() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Anime> criteriaQuery = criteriaBuilder.createQuery(Anime.class);
        Root<Anime> animeRoot = criteriaQuery.from(Anime.class);
        criteriaQuery.orderBy(criteriaBuilder.desc((animeRoot).get("name")));
        List<Anime> anime = entityManager.createQuery(criteriaQuery).getResultList();
        return anime.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList());
    }

    //to get a single field by id
    @Override
    public AnimeResponse getAnimeByIdc(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Anime> criteriaQuery = criteriaBuilder.createQuery(Anime.class);
        Root<Anime> root = criteriaQuery.from(Anime.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id));
        TypedQuery<Anime> query = entityManager.createQuery(criteriaQuery);
        List<Anime> resultList = query.getResultList();
        return resultList.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList()).get(0);
    }
//    Note – same way we can retrieve the entity on basis of othere filed. for example we can retrive can entity on basis of name.
//
//            criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name ));

    //using or in CriteriaBuilder
    @Override
    public List<AnimeResponse> getByIdOrName(Long id, String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Anime> query = criteriaBuilder.createQuery(Anime.class);
        Root<Anime> root = query.from(Anime.class);
        query.select(root);
        Predicate predicateId = criteriaBuilder.equal(root.get("id"), id);
        Predicate predicateName = criteriaBuilder.equal(root.get("name"), name);
        Predicate or = criteriaBuilder.or(predicateId, predicateName);
        query.where(or);
        TypedQuery<Anime> query1 = entityManager.createQuery(query);
        List<Anime> resultList = query1.getResultList();
        return resultList.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList());
    }

    //using and in criteriaBuilder
    @Override
    public List<AnimeResponse> getByNameAndRating(String name, Float rating) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Anime> criteriaQuery = criteriaBuilder.createQuery(Anime.class);
        Root<Anime> root = criteriaQuery.from(Anime.class);
        criteriaQuery.select(root);
        Predicate predicateName = criteriaBuilder.equal(root.get("name"), name);
        Predicate predicateRating = criteriaBuilder.equal(root.get("rating"), rating);
        Predicate and = criteriaBuilder.and(predicateName, predicateRating);
        criteriaQuery.where(and);
        TypedQuery<Anime> query = entityManager.createQuery(criteriaQuery);
        List<Anime> resultList = query.getResultList();
        return resultList.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList());
    }
//using greater than gt using criteriaBuilder

    @Override
    public List<AnimeResponse> getIdGreaterThan(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Anime> criteriaQuery = criteriaBuilder.createQuery(Anime.class);
        Root<Anime> root = criteriaQuery.from(Anime.class);
        criteriaQuery.select(root);
        Predicate id1 = criteriaBuilder.gt(root.get("id"), id);
        criteriaQuery.where(id1);
        TypedQuery<Anime> query = entityManager.createQuery(criteriaQuery);
        List<Anime> resultList = query.getResultList();
        return resultList.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList());
    }

    //criteriaBuilder for greater than equal
    @Override
    public List<AnimeResponse> getIdGreaterThanEqualTo(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Anime> criteriaQuery = criteriaBuilder.createQuery(Anime.class);
        Root<Anime> root = criteriaQuery.from(Anime.class);
        criteriaQuery.select(root);
        Predicate id1 = criteriaBuilder.ge(root.get("id"), id);
        criteriaQuery.where(id1);
        TypedQuery<Anime> query = entityManager.createQuery(criteriaQuery);
        List<Anime> resultList = query.getResultList();
        return resultList.stream().map(AnimeHelper::animeResponseFromAnime).collect(Collectors.toList());
    }

    @Override
    public AnimeResponse getAnimeById(long id) throws ResourceNotFoundException {
        Anime anime = animeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("anime", "id", id));
        return AnimeHelper.animeResponseFromAnime(anime);

    }

    @Override
    public AnimeResponse updateAnime(Long id, AnimeRequest animeRequest) throws ResourceNotFoundException {
        Anime animeToUpdate = animeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("anime", "id", id));
        animeToUpdate.setGenre(animeRequest.getGenre());
        animeToUpdate.setName(animeRequest.getName());
        animeToUpdate.setRating(animeRequest.getRating());
//      animeToUpdate.setActive(animeRequest.getActive());
//      animeToUpdate.setUpdatedBy(animeRequest.getUpdatedBy());
//        animeToUpdate.setCreatedOn(animeRequest.getCreatedOn());
//        animeToUpdate.setDeleted(animeRequest.getDeleted());
//        animeToUpdate.setCreatedBy(animeRequest.getCreatedBy());
//        animeToUpdate.setUpdatedOn(animeRequest.getUpdatedOn());
        animeRepo.save(animeToUpdate);
        return AnimeHelper.animeResponseFromAnime(animeToUpdate);
    }

    @Override
    public String softDeleteAnime(Long id) throws ResourceNotFoundException {
        Anime anime = animeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("anime", "id", id));
        anime.setDeleted(Boolean.TRUE);
        animeRepo.save(anime);
        return "soft deleted your record";
    }

    @Override
    public String deleteAnime(Long id) throws ResourceNotFoundException {
        animeRepo.delete(animeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("anime", "id", id)));
//        AnimeResponse animeResponse = AnimeHelper.animeResponseFromAnime(animeRepo.findById(id).get());
        return "Deleted";
    }

//    @Override
//    public List<Anime> softDeletedAnimes() {
//         return animeRepo.getAnimeByDeleted(true);
//    }

    @Override
    public List<Anime> findByDeletedByTrue() {
        return animeRepo.findByDeletedByTrue();
    }

    @Override
    public List<Anime> searchAnime(String query) {
        return animeRepo.searchAnimeByName(query);
    }
}
