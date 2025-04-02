package io.saim.AjouChatBot_BE.repository;

import io.saim.AjouChatBot_BE.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
