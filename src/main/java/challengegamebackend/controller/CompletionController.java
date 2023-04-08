package challengegamebackend.controller;

import challengegamebackend.model.Challenge;
import challengegamebackend.model.User;
import challengegamebackend.service.CompletionService;
import challengegamebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class CompletionController {
    @Autowired
    private CompletionService completionService;
    @Autowired
    private UserService userService;

    /** Add a challenge completion or update a previous one
     *
     * @param challengeId the completed challenge's id
     * @param comment a comment on the completion of the challenge
     * @param imgBase64 image encoded in a base64 String
     * @param imgFormat jpg, png, jpeg...
     * @return a confirmation message
     */
    @PostMapping("/api/completions")
    public String completeChallenge(
            Principal principal,
            @RequestParam long challengeId,
            @RequestParam String comment,
            @RequestParam String imgBase64,
            @RequestParam String imgFormat) throws IOException {
        return completionService.addChallengeCompletion(
                principal.getName(),
                challengeId,
                comment,
                imgBase64,
                imgFormat
        );
    }

    /** Get the comment and the picture of a user's challenge completion if completed,
     *  otherwise empty strings
     *  Ex: /api/completions/getCommentAndPicture?challengeId=1&userId=1
     *  Ex: /api/completions/getCommentAndPicture?challengeId=1
     * @param challengeId the id of the category
     * @return List of string containing the if the challenge is valid, the comment and the image
     */
    @GetMapping("/api/completions/getCommentAndPicture")
    public List<String> getCommentAndPicture(
            Principal principal,
            @RequestParam Long challengeId,
            @RequestParam(required = false) Long userId
    ) {
        if (userId != null) {
            User user = userService.getUserById(userId);
            return completionService.getCommentAndPicture(user.getUsername(), challengeId);
        }

        return completionService.getCommentAndPicture(principal.getName(), challengeId);
    }

    /** Returns all challenges completed by user in a certain category
     * Ex:  - /api/completions/challenges?userId=1&categoryId=1
     *      - /api/completions/challenges?userId=1&categoryName=Sport
     *      - /api/completions/challenges?userId=1
     *      - /api/completions/challenges?categoryId=1
     *      - /api/completions/challenges?categoryName=Sport
     *      - /api/completions/challenges
     *
     * @param userId     id of User
     * @param categoryId categoryId of the challenges completed by the user
     * @return completed challenges of a certain category as a list
     */
    @GetMapping("/api/completions/challenges")
    public List<Challenge> getCompletedChallengesByCategory(
            Principal principal,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName
    ) {
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (categoryId != null)
                return completionService.getCompletedChallengesByCategoryId(user.getUsername(), categoryId);
            if (categoryName != null)
                return completionService.getCompletedChallengesByCategoryName(user.getUsername(), categoryName);

            return completionService.getCompletedChallengesByUserId(userId);
        }

        if (categoryId != null)
            return completionService.getCompletedChallengesByCategoryId(principal.getName(), categoryId);
        if (categoryName != null)
            return completionService.getCompletedChallengesByCategoryName(principal.getName(), categoryName);

        return completionService.getCompletedChallengesByUsername(principal.getName());
    }

}
