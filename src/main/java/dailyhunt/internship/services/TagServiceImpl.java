package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.request.TagIdList;
import dailyhunt.internship.clientmodels.response.AllTags;
import dailyhunt.internship.clientmodels.response.ImageDTO;
import dailyhunt.internship.clientmodels.response.NewsComponentsRequest;
import dailyhunt.internship.entities.newscomponents.Locality;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;
import dailyhunt.internship.repositories.news.components.TagRepository;
import dailyhunt.internship.services.interfaces.TagService;
import dailyhunt.internship.util.DailyhuntUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          WebClient.Builder webClientBuilder) {
        this.tagRepository = tagRepository;
        this.webClientBuilder = webClientBuilder;
    }


    @Override
    public Tag findTagsById(Long id) throws ResourceNotFoundException {
        Optional<Tag> locality = tagRepository.findById(id);
        if(!locality.isPresent())
            throw new ResourceNotFoundException("This Tag does not exist");
        return locality.get();
    }

    @Override
    public AllTags findAllTags() {

        return AllTags.builder()
                .all_the_tags(tagRepository.findAll())
                .build();
    }

    @Override
    public AllTags findTagsByIds(TagIdList tagIdList) {
        return AllTags.builder()
                .all_the_tags(tagRepository.findAllById(tagIdList.getTagIds()))
                .build();
    }

    @Override
    public Tag saveTags(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        return tag.orElseGet(() -> tagRepository.save(
                Tag.builder()
                .active(true)
                .name(name)
                .postCount(0L)
                .build()
        ));
    }

    @Override
    public void saveTagAtUserProfile(Tag tag) {
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
        String fooResourceUrl = "https://dailyhunt-user-profile.herokuapp.com/api/v1/user_profile/newsComponents/tag";

        String result = webClientBuilder.build()
                .post()
                .uri(fooResourceUrl)
                .body(Mono.just(newsComponentsRequest), NewsComponentsRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public Tag saveTags(NewsComponents tags) {
        return saveTags(tags.getName());
    }

    @Override
    public Tag updateTagName(NewsComponents request, Long tagsId)
            throws BadRequestException, ResourceNotFoundException {
        Optional<Tag> checkTag = tagRepository.findById(tagsId);
        if(tagRepository.existsByName(request.getName()))
            throw new BadRequestException("Tag with this name already exists");
        if(checkTag.isPresent()) {
            Tag tag = checkTag.get();
            tag.setName(request.getName());
            return tagRepository.save(tag);
        }
        throw new ResourceNotFoundException("Invalid Tag selected.");
    }

    @Override
    public Tag updateTagActive(Long tagsId) throws ResourceNotFoundException {
        Optional<Tag> checkTag = tagRepository.findById(tagsId);
        if(checkTag.isPresent()) {
            Tag tag = checkTag.get();
            tag.setActive(!tag.getActive());
            return tagRepository.save(tag);
        }
        throw new ResourceNotFoundException("Invalid Tag selected.");
    }

    @Override
    public void incrementTagPostCount(Tag tag) {
        tag.setPostCount(tag.getPostCount() + 1);
    }

    @Override
    public Boolean deleteTags(Long tagsId) throws ResourceNotFoundException {
        Optional<Tag> tags = tagRepository.findById(tagsId);
        if(tags.isPresent()) {
            deleteTagAtUserProfile(tagsId);
            tagRepository.deleteById(tagsId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid tags selected");
    }

    public void deleteTagAtUserProfile(Long tagId) {
        Tag tag = findTagsById(tagId);
        NewsComponentsRequest newsComponentsRequest = NewsComponentsRequest.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();

        String fooResourceUrl = "http://profile-service/api/v1/user_profile/newsComponents/tag";

        String result = webClientBuilder.build()
                .delete()
                .uri(fooResourceUrl+"/"+tagId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<Tag> findAllById(List<Long> ids) {

        return tagRepository.findAllById(ids);
    }

    @Override
    public Optional<Tag> findTagByName(String name){

        return tagRepository.findByName(name);
    }

}
