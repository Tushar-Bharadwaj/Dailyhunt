package dailyhunt.internship.services;

import dailyhunt.internship.clientmodels.request.NewsComponents;
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

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public Tag findTagsById(Long id) throws ResourceNotFoundException {
        Optional<Tag> locality = tagRepository.findById(id);
        if(!locality.isPresent())
            throw new ResourceNotFoundException("This Tag does not exist");
        return locality.get();
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag saveTags(String name) {
        return saveTags(NewsComponents.builder().name(name).build());
    }

    @Override
    public Tag saveTags(NewsComponents tags) {
        Optional<Tag> tag = tagRepository.findByName(tags.getName());
        return tag.orElseGet(() -> tagRepository.save(
                Tag.builder()
                        .active(true)
                        .name(tags.getName())
                        .postCount(0L)
                        .build()
        ));
    }




    @Override
    public Tag updateTags(NewsComponents tags, Long tagsId) throws ResourceNotFoundException {
        Optional<Tag> checkTag = tagRepository.findById(tagsId);
        if(checkTag.isPresent()) {
            if(DailyhuntUtil.isNullOrEmpty(tags.getActive()))
                throw new BadRequestException("Please fill all fields");
            Tag tag = checkTag.get();
            tag.setActive(tags.getActive());
            tag.setPostCount(tags.getPostCount());
            return tagRepository.save(tag);
        }
        throw new ResourceNotFoundException("Invalid Tag selected.");
    }

    @Override
    public Boolean deleteTags(Long tagsId) throws ResourceNotFoundException {
        Optional<Tag> tags = tagRepository.findById(tagsId);
        if(tags.isPresent()) {
            tagRepository.deleteById(tagsId);
            return true;
        }
        throw new ResourceNotFoundException("Invalid tags selected");
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
