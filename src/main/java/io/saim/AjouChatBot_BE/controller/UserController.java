package io.saim.AjouChatBot_BE.controller;

import io.saim.AjouChatBot_BE.model.User;
import io.saim.AjouChatBot_BE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	//모든 사용자 조회
	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	//사용자 추가
	@PostMapping
	public User createUser(@RequestBody User user) {
		System.out.println("[API 요청] 사용자 추가: " + user.getName() + ", 나이: " + user.getAge());
		User savedUser = userRepository.save(user);
		System.out.println("[MongoDB 저장 완료] ID: " + savedUser.getId());
		return savedUser;
	}
}
