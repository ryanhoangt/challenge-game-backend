package challengegamebackend.service;

import challengegamebackend.dto.UserDTO;
import challengegamebackend.model.Challenge;
import challengegamebackend.model.Completion;
import challengegamebackend.model.User;
import challengegamebackend.repository.CompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CompletionService {
    @Autowired
    private CompletionRepository completionRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChallengeService challengeService;

    /** Add a challenge completion or update a previous one
     *
     * @param imgBase64 the image encoded in a base64 String
     * @param imgFormat jpg, png or jpeg
     * @return confirmation message
     * @throws IOException
     */
    public String addChallengeCompletion(
            String username,
            long challengeId,
            String comment,
            String imgBase64,
            String imgFormat) throws IOException {
        User user = userService.getUserByUsername(username);
        Challenge challenge = challengeService.getChallengeById(challengeId);
        Completion curCompletion;

        if (completionRepository.findByUserAndChallenge(user, challenge).isPresent()) {
            curCompletion = completionRepository.findByUserAndChallenge(user, challenge).get();
        } else
            curCompletion = new Completion();

        curCompletion.setChallenge(challenge);
        curCompletion.setUser(user);
        curCompletion.setComment(comment);

        if (imgBase64.length() > 0) {
            Long userId = userService.getUserByUsername(username).getId();
            String destPath = "resources/img/completion/" + userId
                    + "_" + challengeId
                    + "_" + ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss"))
                    + "." + imgFormat;
            byte[] byteImg = Base64.getDecoder().decode(imgBase64);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(byteImg));
            ImageIO.write(img, "jpg", new File("src/main/" + destPath));

            File previousImg = new File("src/main/" + curCompletion.getImgPath());
            if (previousImg.isFile()) {
                previousImg.delete();
            }
            curCompletion.setImgPath(destPath);
        }

        completionRepository.save(curCompletion);
        return "User " + username + " has completed " + challenge.getName();
    }

    public List<String> getCommentAndPicture(String username, long challengeId) {
        List<String> res = new ArrayList<>();
        User user = userService.getUserByUsername(username);
        Challenge challenge = challengeService.getChallengeById(challengeId);
        Optional<Completion> opt = completionRepository.findByUserAndChallenge(user, challenge);

        if (!opt.isPresent()) return res;
        Completion completion = opt.get();
        res.add(completion.getComment());
        res.add(completion.getImgPath());
        return res;
    }

    public List<Challenge> getCompletedChallengesByCategoryId(String username, long categoryId) {
        List<Challenge> res = new ArrayList<>();
        User user = userService.getUserByUsername(username);
        for (Completion compItem : completionRepository.findByUser(user)) {
            if (compItem.getChallenge().getCategoryId() == categoryId)
                res.add(compItem.getChallenge());
        }
        return res;
    }

    public List<Challenge> getCompletedChallengesByCategoryName(String username, String categoryName) {
        return getCompletedChallengesByCategoryId(username, categoryService.getIdByName(categoryName));
    }

    /**
     * @param challengeId id of challenge considered
     * @return a list of (username, comment) pairs
     */
    public List<List<String>> getCommentsOfChallenge(long challengeId) {
        Challenge challenge = challengeService.getChallengeById(challengeId);

        List<List<String>> res = new ArrayList<>();
        for (Completion compItem : completionRepository.findByChallenge(challenge)) {
            if (compItem.getComment() != null && compItem.getComment().length() > 0) {
                List<String> userNameAndCommentPair = new ArrayList<>();
                userNameAndCommentPair.add(compItem.getUser().getUsername());
                userNameAndCommentPair.add(compItem.getComment());
                res.add(userNameAndCommentPair);
            }
        }
        return res;
    }

    public long getNumberOfCompletedChallengesByUserId(long userId){
        return getCompletedChallengesByUserId(userId).size();
    }

    public List<Challenge> getCompletedChallengesByUserId(long userId) {
        List<Challenge> res = new ArrayList<>();
        User user = userService.getUserById(userId);
        for (Completion compItem : completionRepository.findByUser(user)) {
            res.add(compItem.getChallenge());
        }
        return res;
    }

    public List<Challenge> getCompletedChallengesByUsername(String username) {
        List<Challenge> res = new ArrayList<>();
        User user = userService.getUserByUsername(username);
        for (Completion compItem : completionRepository.findByUser(user)) {
            res.add(compItem.getChallenge());
        }
        return res;
    }

    public List<UserDTO> getCompletersOfChallenge(long challengeId) {
        List<UserDTO> res = new ArrayList<>();
        Challenge c = challengeService.getChallengeById(challengeId);
        for (Completion compItem : completionRepository.findByChallenge(c)) {
            res.add(new UserDTO(compItem.getUser()));
        }
        return res;
    }

}
