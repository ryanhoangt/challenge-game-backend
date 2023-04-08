package challengegamebackend.controller;

import challengegamebackend.dto.UserDTO;
import challengegamebackend.dto.UserWithCompletionCount;
import challengegamebackend.model.User;
import challengegamebackend.service.FriendshipService;
import challengegamebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendshipController {
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private UserService userService;

    /**
     * Returns the list of all the friends of the current user ordered by the number of completed Challenges
     * Ex: /api/friendships/friendsWithCompletionCount?sort_by=completionCount&sort_order=desc
     *
     * @param principal needed to know who is the current user
     * @return A list with the friends of the current user ordered by the number of completed Challenges
     */
    @GetMapping("/api/friendships/friendsWithCompletionCount")
    public List<UserWithCompletionCount> getFriendsOrderByCompletionCount(
            Principal principal,
            @RequestParam("sort_by") String sortBy,
            @RequestParam("sort_order") String sortOrder
    ) {
        if (sortBy == "completionCount" && sortOrder == "desc") {
            User user = userService.getUserByUsername(principal.getName());
            return friendshipService.getFriendsOrderByCompletionCountDesc(user);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request parameters");
    }

    /**
     * Returns the list of all the user that asks the current user to be friend
     *
     * @param principal needed to know who is the current user
     * @return the list of all the user that asks the current user to be friend
     */
    @GetMapping("/api/friendships/inRequests")
    public List<UserDTO> getFriendsRequests(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User u : friendshipService.getInRequestSenders(user))
            userDTOS.add(new UserDTO(u));
        return userDTOS;
    }

    /**
     * Tell if user1 is friend with user2
     * user1 is friend with user 2 iff user2 is friend with user1
     * Ex: /api/friendship/isFriend?user1Id=12&user2Id=34
     *
     * @return true if it is the case false otherwise
     */
    @GetMapping("/api/friendships/isFriendOrNot")
    public boolean isFriendOrNot(@RequestParam long user1Id, @RequestParam long user2Id) {
        return friendshipService.isFriendOrNot(user1Id, user2Id);
    }

    /**
     * The current user asks the user given in parameter to be his friend
     * Ex: /api/friendships/requests?username=david
     *
     * @param principal needed to now who is the current user
     * @param username the username of the user to which we want to ask to be her/his friend
     * @return a comment on the post request
     */
    @PostMapping("/api/friendships/sendOutRequest")
    public String sendFriendRequest(Principal principal, @RequestParam String username) {
        User user = userService.getUserByUsername(principal.getName());
        return friendshipService.sendOutRequest(user, username);
    }

    /**
     * Accept the pending request previously done by the user given in argument
     * Ex: /api/friendships/acceptances?userId=23
     *
     * @param principal needed to kwon who is the current user
     * @param userId the userId of the user who previously made a friend request
     * @return a comment on the post request
     */
    @PostMapping("/api/friendships/acceptInRequest")
    public String acceptFriendRequest(Principal principal, @RequestParam long userId) {
        User user = userService.getUserByUsername(principal.getName());
        return friendshipService.acceptInRequest(user, userId);
    }

    /**
     * Refuse the pending request previously done by the user given in argument
     * Ex: /api/friendships/refusals?userId=23
     *
     * @param principal needed to know who is the current user
     * @param userId the userId of the user who previously made a friend request
     * @return a comment on the post request
     */
    @PostMapping("/api/friendships/refuseInRequest")
    public String refuseFriendRequest(Principal principal, @RequestParam long userId) {
        User user = userService.getUserByUsername(principal.getName());
        return friendshipService.refuseInRequest(user ,userId);
    }

}
