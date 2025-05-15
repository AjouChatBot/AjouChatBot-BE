package io.saim.AjouChatBot_BE.chat.service;
import java.util.List;

import io.saim.AjouChatBot_BE.chat.dto.ChatSettingResponseDTO;
import io.saim.AjouChatBot_BE.chat.dto.ChatSettingUpdateRequestDTO;
import io.saim.AjouChatBot_BE.chat.dto.RecentTopicResponseDTO;
import io.saim.AjouChatBot_BE.chat.entity.RecentTopic;
import io.saim.AjouChatBot_BE.chat.dto.ChatHistoryResponseDTO;
import io.saim.AjouChatBot_BE.chat.dto.ChatMessageDTO;
import io.saim.AjouChatBot_BE.chat.entity.ChatMessage;
import io.saim.AjouChatBot_BE.chat.repository.ChatMessageRepository;

import io.saim.AjouChatBot_BE.chat.repository.ChatSettingRepository;
import io.saim.AjouChatBot_BE.chat.repository.RecentTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final ChatMessageRepository chatMessageRepository;
	private final RecentTopicRepository recentTopicRepository;
	private final ChatSettingRepository chatSettingRepository;

	public Mono<ChatHistoryResponseDTO> getChatHistory(String conversationId) {
		return chatMessageRepository.findByConversationId(conversationId)
			.map(msg -> new ChatMessageDTO(msg.getSender(), msg.getMessage(), msg.getTimestamp()))
			.collectList()
			.map(messages -> {
				ChatHistoryResponseDTO.DataContent data = new ChatHistoryResponseDTO.DataContent();
				data.setConversation_id(conversationId);
				data.setMessages(messages);

				ChatHistoryResponseDTO response = new ChatHistoryResponseDTO();
				response.setData(data);
				return response;
			});
	}

	public Flux<RecentTopicResponseDTO> getRecentTopics() {
		return recentTopicRepository.findAllByOrderByCreatedAtDesc()
			.map(t -> new RecentTopicResponseDTO(
				t.getQuestionId(),
				t.getQuestion(),
				t.getCreatedAt(),
				t.getKeywords()
			));
	}

	public Flux<RecentTopicResponseDTO> search(String query, List<String> keywords, String startDate, String endDate) {
		return recentTopicRepository.search(query, keywords, startDate, endDate)
			.map(t -> new RecentTopicResponseDTO(
				t.getQuestionId(),
				t.getQuestion(),
				t.getCreatedAt(),
				t.getKeywords()
			));
	}

	public Mono<ChatSettingResponseDTO> getChatSettings(String userId) {
		return chatSettingRepository.findByUserId(userId)
			.map(setting -> new ChatSettingResponseDTO(
				setting.isNewTopicQuestion(),
				setting.isIncludeAcademicInfo(),
				setting.isAllowResponse()
			));
	}

	public Mono<Void> updateChatSettings(String userId, ChatSettingUpdateRequestDTO dto) {
		return chatSettingRepository.findByUserId(userId)
			.flatMap(setting -> {
				setting.setNewTopicQuestion(dto.isNew_topic_question());
				setting.setIncludeAcademicInfo(dto.isInclude_academic_info());
				setting.setAllowResponse(dto.isAllow_response());
				return chatSettingRepository.save(setting);
			})
			.then();
	}
}
