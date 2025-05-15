package io.saim.AjouChatBot_BE.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path uploadDir = Paths.get("uploads"); // 상대 경로

	@Override
	public Mono<String> storeFile(MultipartFile file, String documentType, String userId) {
		String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

		Path destination = uploadDir.resolve(filename);
		return Mono.fromCallable(() -> {
				if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
				return destination;
			})
			.flatMap(path -> {
				try {
					file.transferTo(path);
					return Mono.just("/files/" + filename);
				} catch (IOException e) {
					return Mono.error(new RuntimeException("파일 저장 실패", e));
				}
			});
	}
}
