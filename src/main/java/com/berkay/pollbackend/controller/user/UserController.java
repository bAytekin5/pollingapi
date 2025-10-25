package com.berkay.pollbackend.controller.user;

import com.berkay.pollbackend.repository.PollRepository;
import com.berkay.pollbackend.repository.UserRepository;
import com.berkay.pollbackend.repository.VoteRepository;
import com.berkay.pollbackend.service.PollService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // TODO
}
