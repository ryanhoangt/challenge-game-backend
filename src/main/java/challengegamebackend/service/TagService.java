package challengegamebackend.service;

import challengegamebackend.model.Tag;
import challengegamebackend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(long tagId) {
        return checkTagExistsAndGet(tagRepository.findById(tagId), "id", "" + tagId);
    }

    public Tag getTagByName(String tagName) {
        return checkTagExistsAndGet(tagRepository.findByName(tagName), "name", "" + tagName);
    }

    public void addTag(Tag tag) {
        tagRepository.save(tag);
    }

    private Tag checkTagExistsAndGet(Optional<Tag> opt, String key, String value) {
        if (!opt.isPresent())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Tag with " + key + ": " + value + " not found"
            );
        return opt.get();
    }
}
