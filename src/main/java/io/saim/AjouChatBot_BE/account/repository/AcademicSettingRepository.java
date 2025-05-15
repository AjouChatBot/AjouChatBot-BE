package io.saim.AjouChatBot_BE.account.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import io.saim.AjouChatBot_BE.account.entity.AcademicSetting;

public interface AcademicSettingRepository extends ReactiveMongoRepository<AcademicSetting, String> {
}
