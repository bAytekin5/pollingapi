package com.berkay.pollbackend.service;

import com.berkay.pollbackend.dto.paged.PagedResponse;
import com.berkay.pollbackend.dto.poll.PollRequest;
import com.berkay.pollbackend.dto.poll.PollResponse;
import com.berkay.pollbackend.dto.vote.VoteRequest;
import com.berkay.pollbackend.model.Poll;
import com.berkay.pollbackend.security.UserPrincipal;
import jakarta.validation.Valid;

public interface PollService {
    PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size);

    Poll createPoll(PollRequest pollRequest);

    PollResponse getPollById(Long pollId, UserPrincipal currentUser);

    PollResponse castVoteAndGetUpdatedPoll(Long pollId, @Valid VoteRequest voteRequest, UserPrincipal currentUser);

    PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size);
}
