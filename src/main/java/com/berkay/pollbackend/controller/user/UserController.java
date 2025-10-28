package com.berkay.pollbackend.controller.user;

import com.berkay.pollbackend.dto.paged.PagedResponse;
import com.berkay.pollbackend.dto.poll.PollResponse;
import com.berkay.pollbackend.dto.user.UserIdentityAvailability;
import com.berkay.pollbackend.dto.user.UserProfile;
import com.berkay.pollbackend.dto.user.UserSummary;
import com.berkay.pollbackend.exception.ResourceNotFoundException;
import com.berkay.pollbackend.model.User;
import com.berkay.pollbackend.repository.PollRepository;
import com.berkay.pollbackend.repository.UserRepository;
import com.berkay.pollbackend.repository.VoteRepository;
import com.berkay.pollbackend.security.CurrentUser;
import com.berkay.pollbackend.security.UserPrincipal;
import com.berkay.pollbackend.service.PollService;
import com.berkay.pollbackend.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;
    private final PollService pollService;

    public UserController(UserRepository userRepository, PollRepository pollRepository, VoteRepository voteRepository, PollService pollService) {
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
        this.pollService = pollService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);

    }

    @GetMapping("/user/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        return new UserProfile(user.getId(), user.getUsername()
                , user.getName(), user.getCreatedAt(), pollCount, voteCount);
    }

    @GetMapping("/users/{username}/polls")
    public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int size){
        return pollService.getPollsCreatedBy(username,currentUser,page,size);
    }
}
