package com.berkay.pollbackend.util;

import com.berkay.pollbackend.dto.choice.ChoiceResponse;
import com.berkay.pollbackend.dto.poll.PollResponse;
import com.berkay.pollbackend.dto.user.UserSummary;
import com.berkay.pollbackend.model.Poll;
import com.berkay.pollbackend.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ModelMapper {

    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setQuestion(poll.getQuestion());
        response.setCreationDateTime(poll.getCreatedAt());
        response.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        response.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if (choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).toList();

        response.setChoices(choiceResponses);
        UserSummary createSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        response.setCreatedBy(createSummary);

        if (userVote != null) {
            response.setSelectedChoice(userVote);
        }

        long totalVotes = response.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        response.setTotalVotes(totalVotes);

        return response;
    }
}
