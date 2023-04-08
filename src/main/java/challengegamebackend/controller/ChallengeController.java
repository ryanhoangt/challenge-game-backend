package challengegamebackend.controller;

import challengegamebackend.dto.UserDTO;
import challengegamebackend.model.Challenge;
import challengegamebackend.model.Tag;
import challengegamebackend.service.ChallengeService;
import challengegamebackend.service.ChallengeToTagService;
import challengegamebackend.service.CompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private CompletionService completionService;
    @Autowired
    private ChallengeToTagService challengeToTagService;

    /**
     * Add a challenge given as argument to the database
     *
     * @param challenge a challenge we want to add
     */
    @PostMapping(path = "/api/challenges")
    public void addChallenge(@RequestBody Challenge challenge) {
        challengeService.addChallenge(challenge);
    }

    /**
     * Retrieve all users that complete challenge Challenge
     *
     * @param challengeId the id of Challenge
     * @return completers as a list
     */
    @GetMapping("/api/challenges/{challengeId}/completions/users")
    public List<UserDTO> getCompletersOfChallenge(@PathVariable long challengeId) {
        return completionService.getCompletersOfChallenge(challengeId);
    }

    /**
     * Return a list with all the comments on a specified challenge
     *
     * @param challengeId the id of a challenge
     * @return a list of (user, comment) pairs
     */
    @GetMapping("/api/challenges/{challengeId}/completions/comments")
    public List<List<String>> getCommentsOfChallenge(@PathVariable long challengeId) {
        return completionService.getCommentsOfChallenge(challengeId);
    }

    /**
     * Returns all the challenges (with completion status) stored in the database
     * Ex:  - /api/challengesWithCompletionStatus
     *      - /api/challengesWithCompletionStatus?categoryId=12
     *
     * @return a list that contains all the challenges stored in the database
     */
    @GetMapping("/api/challengesWithCompletionStatus")
    public Map<String, List> allChallenges(
            Principal principal,
            @RequestParam Long categoryId
    ) {
        if (categoryId != null) {
            return challengeService.getChallengesByCategoryIdWithStatus(
                    principal.getName(),
                    categoryId
            );
        }

        return challengeService.getAllChallengesWithCompletionStatus(principal.getName());
    }

    /**
     * Returns  - all the challenges stored in the database
     *          - a list with all the challenges that are in a category
     *          - all Challenges that have the tag with given tagId
     * Ex:  - /api/challenges
     *      - /api/challenges?categoryId=12
     *      - /api/challenges?tagId=4
     *
     * @return a list that contains all the challenges stored in the database
     */
    @GetMapping("/api/challenges")
    public List<Challenge> allChallenges(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Long tagId
    ) {
        if (tagId != null)
            return challengeToTagService.getChallengesHavingTag(tagId);

        if (categoryId != null)
            return challengeService.getChallengesByCategoryId(categoryId);

        if (categoryName != null)
            return challengeService.getChallengesByCategoryName(categoryName);

        return challengeService.getAllChallenges();
    }

    /**
     * Returns at max 9 random challenges stored in the database
     * Ex:  - /api/challenges/nineRandom?withCompletionStatus=true
     *      - /api/challenges/nineRandom
     *
     * @return a list that contains all the challenges stored in the database
     */
    @GetMapping("/api/challenges/nineRandom")
    public Map<String, List> nineChallenges(Principal principal, @RequestParam(defaultValue = "false") boolean withCompletionStatus) {
        if (withCompletionStatus) {
            return challengeService.getNineRandomWithCompletionStatus(principal.getName());
        }

        return challengeService.getNineRandom();
    }

    /**
     * Returns an Optional that contains a challenge with a specified id if it exists
     * otherwise returns an empty Optional
     *
     * @param challengeId the id of a challenge
     * @return the challenge with the specified id
     */
    @GetMapping("/api/challenges/{challengeId}")
    public Challenge getChallenge(@PathVariable long challengeId) {
        return challengeService.getChallengeById(challengeId);
    }

    /**
     * Returns - the number of challenges
     *         - the number of challenges of a given category's name
     *         - the number of challenges of a given category id
     * Ex:  - /api/challenges/count
     *      - /api/challenges/count?categoryName=sport
     *      - /api/challenges/count?categoryId=2
     * @param categoryName the name of the category
     * @return the number of challenges in the category
     */
    @GetMapping("/api/challenges/count")
    public Long numberOfChallengesBy(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName
    ) {
        if (categoryName != null)
            return challengeService.numberOfChallengesByCategoryName(categoryName);

        if (categoryId != null)
            return challengeService.numberOfChallengesByCategoryId(categoryId);

        return challengeService.numberOfChallenges();
    }

    /**
     * Returns an Optional that contains a challenge with a specified name if it exists
     * otherwise returns an empty Optional
     * Ex: /api/challenges?name=workout-10-mins
     *
     * @param name the name of a challenge
     * @return the challenge with the specified name
     */
    @GetMapping("/api/challenges/byName/{name}")
    public Challenge getChallengeBy(@RequestParam String name) {
        return challengeService.getChallengeByName(name);
    }

    /**
     * Retrieve all the tag that the challenge with challengeId has
     *
     * @param challengeId the id of the challenge of interest
     * @return a list of all the tags that are linked to this challenge
     */
    @GetMapping("/api/challenges/{challengeId}/tags")
    public List<Tag> getTagsOfChallenge(@PathVariable long challengeId) {
        return challengeToTagService.getTagsOfChallenge(challengeId);
    }

}
