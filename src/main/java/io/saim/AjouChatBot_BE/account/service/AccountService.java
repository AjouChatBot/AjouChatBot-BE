package io.saim.AjouChatBot_BE.account.service;

import io.saim.AjouChatBot_BE.account.entity.AccountInfo;
import io.saim.AjouChatBot_BE.account.repository.AccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountInfoRepository accountInfoRepository;

	public Mono<AccountInfo> getAccountInfo(String userId) {
		return accountInfoRepository.findById(userId);
	}
}
