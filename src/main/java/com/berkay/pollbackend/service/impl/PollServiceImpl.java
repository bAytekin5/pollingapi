package com.berkay.pollbackend.service.impl;

import com.berkay.pollbackend.dto.paged.PagedResponse;
import com.berkay.pollbackend.dto.poll.PollRequest;
import com.berkay.pollbackend.dto.poll.PollResponse;
import com.berkay.pollbackend.dto.vote.VoteRequest;
import com.berkay.pollbackend.model.Poll;
import com.berkay.pollbackend.security.UserPrincipal;
import com.berkay.pollbackend.service.PollService;
import org.springframework.stereotype.Service;

// TODO

@Service
public class PollServiceImpl implements PollService {
    @Override
    public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
        return null;
    }

    @Override
    public Poll createPoll(PollRequest pollRequest) {
        return null;
    }

    @Override
    public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {
        return null;
    }

    @Override
    public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
        return null;
    }
}
