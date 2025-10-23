package com.berkay.pollbackend.repository;

import com.berkay.pollbackend.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceReposityory extends JpaRepository<Choice, Long> {
}
