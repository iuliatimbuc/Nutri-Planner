package com.example.nutriplanner.repository;

import com.example.nutriplanner.model.Message;
import com.example.nutriplanner.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(User sender, User receiver, User receiver2, User sender2);
}