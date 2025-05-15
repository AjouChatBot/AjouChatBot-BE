package io.saim.AjouChatBot_BE.account.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import io.saim.AjouChatBot_BE.account.entity.AccountInfo;
import reactor.core.publisher.Mono;

public interface AccountInfoRepository extends ReactiveMongoRepository<AccountInfo, String> {
	Mono<AccountInfo> findById(String id); //ex) "user123"
}
