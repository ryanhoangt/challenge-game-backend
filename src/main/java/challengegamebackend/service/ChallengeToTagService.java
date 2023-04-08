package challengegamebackend.service;

import challengegamebackend.model.Challenge;
import challengegamebackend.model.ChallengeToTag;
import challengegamebackend.model.Tag;
import challengegamebackend.repository.ChallengeToTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeToTagService {
    @Autowired
    private TagService tagService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ChallengeToTagRepository challengeToTagRepository;

    public List<Tag> getTagsOfChallenge(long challengeId) {
        List<Tag> res = new ArrayList<>();
        Challenge curChallenge = challengeService.getChallengeById(challengeId);
        for (ChallengeToTag item: challengeToTagRepository.findByChallenge(curChallenge)) {
            res.add(item.getTag());
        }
        return res;
    }

    public String addTagToChallenge(long tagId, long challengeId) {
        Tag curTag = tagService.getTagById(tagId);
        Challenge curChallenge = challengeService.getChallengeById(challengeId);
        ChallengeToTag item = new ChallengeToTag();
        item.setChallenge(curChallenge);
        item.setTag(curTag);
        challengeToTagRepository.save(item);
        return "Tag " + curTag.getName() + " is a tag of challenge " + curChallenge.getName();
    }

    public List<Challenge> getChallengesHavingTag(long tagId) {
        List<Challenge> res = new ArrayList<>();
        Tag curTag = tagService.getTagById(tagId);
        for (ChallengeToTag item: challengeToTagRepository.findByTag(curTag)) {
            res.add(item.getChallenge());
        }
        return res;
    }

}
