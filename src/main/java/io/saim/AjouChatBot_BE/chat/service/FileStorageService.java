package io.saim.AjouChatBot_BE.chat.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface FileStorageService {
	Mono<String> storeFile(MultipartFile file, String documentType, String userId);
}
