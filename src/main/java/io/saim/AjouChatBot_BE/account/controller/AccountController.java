package io.saim.AjouChatBot_BE.account.controller;

import io.saim.AjouChatBot_BE.account.entity.AccountInfo;
import io.saim.AjouChatBot_BE.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	@GetMapping("/info")
	public Mono<Map<String, Object>> getAccountInfo() {
		String mockUserId = "user123"; //Authorization 후 교체

		return accountService.getAccountInfo(mockUserId)
			.map(info -> Map.of(
				"status", "success",
				"data", info
			));
	}
}
