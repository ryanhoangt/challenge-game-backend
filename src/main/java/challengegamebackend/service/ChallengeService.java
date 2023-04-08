package challengegamebackend.service;

import challengegamebackend.model.Category;
import challengegamebackend.model.Challenge;
import challengegamebackend.model.User;
import challengegamebackend.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChallengeService {
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CompletionService completionService;

    // === GET ===
    public Challenge getChallengeById(long challengeId) {
        return checkChallengeExistsAndGet(challengeRepository.findById(challengeId), "id", "" + challengeId);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Challenge getChallengeByName(String name) {
        return checkChallengeExistsAndGet(challengeRepository.findByName(name),"name", ""+name);
    }

    public List<Challenge> getChallengesByCategoryId(long categoryId) {
        return challengeRepository.findByCategoryId(categoryId);
    }

    public List<Challenge> getChallengesByCategoryName(String name) {
        return challengeRepository.findByCategoryId(categoryService.getIdByName(name));
    }

    // === POST ===
    public void addChallenge(Challenge challenge) {
        challengeRepository.save(challenge);
    }

    public long numberOfChallenges() {
        return challengeRepository.count();
    }

    public Long numberOfChallengesByCategoryId(long category) {
        return challengeRepository.countByCategoryId(category);
    }

    public Long numberOfChallengesByCategoryName(String name) {
        return challengeRepository.countByCategoryId(categoryService.getIdByName(name));
    }

    public Map<String, List> getAllChallengesWithCompletionStatus(String username) {
        User user = userService.getUserByUsername(username);

        List<Challenge> allChallenges = challengeRepository.findAll();
        List<Boolean> compStatusList = getCompletionStatusForUser(user.getId(), allChallenges);

        HashMap<String, List> map = new HashMap<>();
        map.put("Challenges", allChallenges);
        map.put("Statuses", compStatusList);
        return map;
    }

    public Map<String, List> getNineRandomWithCompletionStatus(String username) {
        User user = userService.getUserByUsername(username);

        List<Challenge> all = challengeRepository.findAll();
        Collections.shuffle(all);
        List<Challenge> nineChallenges = all.subList(0, Math.min(9, all.size()));

        List<Boolean> compStatusList = getCompletionStatusForUser(user.getId(), nineChallenges);

        HashMap<String, List> map = new HashMap<>();
        map.put("Challenges", nineChallenges);
        map.put("Statuses", compStatusList);
        return map;
    }

    public Map<String, List> getNineRandom() {
        List<Challenge> all = challengeRepository.findAll();
        Collections.shuffle(all);
        HashMap<String, List> map = new HashMap<>();
        map.put("Challenges", all.subList(0, Math.min(9, all.size())));
        return map;
    }

    public Map<String, List> getChallengesByCategoryIdWithStatus(String username, long categoryId) {
        User user = userService.getUserByUsername(username);
        List<Challenge> filteredChallenges = getChallengesByCategoryId(categoryId);
        List<Boolean> compStatusList = getCompletionStatusForUser(user.getId(), filteredChallenges);

        HashMap<String, List> map = new HashMap<>();
        map.put("Challenges", filteredChallenges);
        map.put("Statuses", compStatusList);
        return map;
    }

    private List<Boolean> getCompletionStatusForUser(long userId, List<Challenge> cs) {
        List<Challenge> completedChallenges = completionService.getCompletedChallengesByUserId(userId);
        Set<Challenge> completedSet = new HashSet<>(completedChallenges);

        return cs.stream().map(
                completedSet::contains).collect(Collectors.toList()
        );
    }

    private Challenge checkChallengeExistsAndGet(Optional<Challenge> opt, String key, String value) {
        if (!opt.isPresent())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Challenge with " + key + ": " + value + " not found"
            );
        return opt.get();
    }

}
