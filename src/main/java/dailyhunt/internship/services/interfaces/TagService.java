package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.clientmodels.request.NewsComponentsActive;
import dailyhunt.internship.clientmodels.request.TagIdList;
import dailyhunt.internship.clientmodels.response.AllTags;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.BadRequestException;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Tag findTagsById(Long id) throws ResourceNotFoundException;

    AllTags findAllTags();

    AllTags findTagsByIds(TagIdList tagIdList);

    Tag saveTags(NewsComponents tags);

    void saveTagAtUserProfile(Tag tag);

    Tag saveTags(String tags);


    Tag updateTagName(NewsComponents request, Long tagId)
            throws BadRequestException, ResourceNotFoundException;

    Tag updateTagActive(Long tagId) throws  ResourceNotFoundException;

    void incrementTagPostCount(Tag tag);

    Boolean deleteTags(Long tagsId) throws ResourceNotFoundException;

    List<Tag> findAllById(List<Long> ids);

    Optional<Tag> findTagByName(String name);

}
