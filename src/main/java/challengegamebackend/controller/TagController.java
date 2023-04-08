package challengegamebackend.controller;

import challengegamebackend.model.Tag;
import challengegamebackend.service.ChallengeToTagService;
import challengegamebackend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private ChallengeToTagService challengeToTagService;

    /**
     * Add a tag given as argument to the database
     *
     * @param tag a tag we want to add
     */
    @PostMapping("/api/tags")
    public void addTag(@RequestBody Tag tag) {
        tagService.addTag(tag);
    }

    /**
     * Return a tag with a given Id
     *
     * @param tagId the id of a tag
     * @return the tag with this associated Id
     */
    @GetMapping("/api/tags/{tagId}")
    public Tag getTagById(@PathVariable long tagId) {
        return tagService.getTagById(tagId);
    }

    /**
     * Return a tag with a given name
     *
     * @param tagName the name of the tag
     * @return the tag that has a given name
     */
    @GetMapping("/api/tags/byName/{tagName}")
    public Tag getTagByName(@PathVariable String tagName) {
        return tagService.getTagByName(tagName);
    }

    /**
     * Returns a list with all the tags
     *
     * @param
     * @return a list with all the tags
     */
    @GetMapping("/api/tags")
    public List<Tag> allTags() {
        return tagService.getAllTags();
    }

    /**
     * Add a tag to a particular challenge
     *
     * @param tagId       the tag we want to link
     * @param challengeId the challenge we want to link to
     * @return a confirmation message
     */
    @PostMapping("/api/tags/challenges")
    public String addTagToChallenge(@RequestParam long tagId, @RequestParam long challengeId) {
        return challengeToTagService.addTagToChallenge(tagId, challengeId);
    }

}
