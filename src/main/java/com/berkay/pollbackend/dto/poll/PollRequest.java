package com.berkay.pollbackend.dto.poll;

import com.berkay.pollbackend.dto.choice.ChoiceRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PollRequest {

    @NotBlank
    @Size(max = 40)
    private String question;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    private List<ChoiceRequest> choices;

    @NotNull
    @Valid
    private PollLength pollLength;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<ChoiceRequest> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceRequest> choices) {
        this.choices = choices;
    }

    public PollLength getPollLenght() {
        return pollLength;
    }

    public void setPollLenght(PollLength pollLenght) {
        this.pollLength = pollLenght;
    }
}
