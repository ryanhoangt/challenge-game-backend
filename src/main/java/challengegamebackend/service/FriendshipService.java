package challengegamebackend.service;

import challengegamebackend.dto.UserWithCompletionCount;
import challengegamebackend.model.Friendship;
import challengegamebackend.model.User;
import challengegamebackend.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CompletionService completionService;

    public List<UserWithCompletionCount> getFriendsOrderByCompletionCountDesc(User user) {
        List<UserWithCompletionCount> friends = new ArrayList<>();
        for(Friendship f : friendshipRepository.findByUser1(user)) {
            if (f.getIsAccepted()) {
                User u = f.getUser2();
                friends.add(new UserWithCompletionCount(
                        u.getId(),
                        u.getUsername(),
                        completionService.getNumberOfCompletedChallengesByUserId(u.getId()))
                );
            }
        }
        for(Friendship f : friendshipRepository.findByUser2(user)) {
            if (f.getIsAccepted()) {
                User u = f.getUser1();
                friends.add(new UserWithCompletionCount(
                        u.getId(),
                        u.getUsername(),
                        completionService.getNumberOfCompletedChallengesByUserId(u.getId()))
                );
            }
        }
        friends.sort((x, y) -> Long.compare(y.getCompletionCount(), x.getCompletionCount()));
        return friends;
    }

    public List<User> getInRequestSenders(User user) {
        List<User> friends = new ArrayList<>();
        for(Friendship f : friendshipRepository.findByUser2(user)) {
            if (!f.getIsAccepted()) {
                friends.add(f.getUser1());
            }
        }
        return friends;
    }

    public boolean isFriendOrNot(long user1Id, long user2Id) {
        User user1 = userService.getUserById(user1Id);
        User user2 = userService.getUserById(user2Id);
        Optional<Friendship> of1 = friendshipRepository.findByUser1AndUser2(user1, user2);
        Optional<Friendship> of2 = friendshipRepository.findByUser1AndUser2(user2, user1);
        return (of1.isPresent() && of1.get().getIsAccepted())  ||
                (of2.isPresent() && of2.get().getIsAccepted());
    }

    public String sendOutRequest(User currentUser, String targetUsername) {
        User targetUser = userService.getUserByUsername(targetUsername);
        if (targetUser.equals(currentUser))
            return "You cannot be friends with yourself";

        Optional<Friendship> of1 = friendshipRepository.findByUser1AndUser2(currentUser, targetUser);
        Optional<Friendship> of2 = friendshipRepository.findByUser1AndUser2(targetUser, currentUser);
        if (of1.isPresent()) {  // if current user already asked target user to be his/her friend
            // and user has or has not already answered
            Friendship f1 = of1.get();
            if (f1.getIsAccepted())
                return currentUser.getUsername() + " and " + targetUser.getUsername() + " are already friends.";
            else
                return "User: " + currentUser.getUsername() + " has previously asked " +
                        targetUser.getUsername() + " to be his/her friend.";
        } else if (of2.isPresent()) { // if target user already asked current user to be his/her friend
            // and currentUser has or has not already answered
            Friendship f2 = of2.get();
            if (f2.getIsAccepted())
                return currentUser.getUsername() + " and " + targetUser.getUsername() + " are already friends.";
            else{
                f2.setIsAccepted(true);
                friendshipRepository.save(f2);
                return currentUser.getUsername() + " and " + targetUser.getUsername() + " are now friends.";
            }
        } else { //if they never asked each other
            Friendship friends = new Friendship();
            friends.setUser1(currentUser);
            friends.setUser2(targetUser);
            friends.setIsAccepted(false);
            friendshipRepository.save(friends);
            return currentUser.getUsername() + " successfully asked " + targetUser.getUsername() + " to be his/her friend.";
        }
    }

    public String acceptInRequest(User currentUser, long targetUserId) {
        User targetUser = userService.getUserById(targetUserId);
        Optional<Friendship> of = friendshipRepository.findByUser1AndUser2(targetUser, currentUser);
        if (!of.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User: " + targetUser.getUsername() + " has not asked " +
                    currentUser.getUsername() + " to be his/her friend.");

        Friendship f = of.get();
        if (f.getIsAccepted())
            return currentUser.getUsername() + " is already friend with " + targetUser.getUsername();
        else {
            f.setIsAccepted(true);
            friendshipRepository.save(f);
            return currentUser.getUsername() + " and " + targetUser.getUsername() + " are now friends";
        }
    }

    public String refuseInRequest(User currentUser, long targetUserId) {
        User targetUser = userService.getUserById(targetUserId);
        Optional<Friendship> of = friendshipRepository.findByUser1AndUser2(targetUser, currentUser);
        if (!of.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User: " + targetUser.getUsername() + " has not asked " +
                    currentUser.getUsername() + " to be his/her friend.");
        } else {
            friendshipRepository.delete(of.get());
            return "Request from " + targetUser.getUsername() + " to " + currentUser.getUsername() + " deleted";
        }
    }

}
