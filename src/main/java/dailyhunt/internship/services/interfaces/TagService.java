package dailyhunt.internship.services.interfaces;

import dailyhunt.internship.clientmodels.request.NewsComponents;
import dailyhunt.internship.entities.newscomponents.Tag;
import dailyhunt.internship.exceptions.ResourceNotFoundException;

import java.util.List;

public interface TagService {
    Tag findTagsById(Long id) throws ResourceNotFoundException;

    List<Tag> findAllTags();

    Tag saveTags(NewsComponents tags);

    Tag updateTags(NewsComponents tags, Long tagsId) throws ResourceNotFoundException;

    Boolean deleteTags(Long tagsId) throws ResourceNotFoundException;

    List<Tag> findAllById(List<Long> ids);
}
