package com.realtime.notificationengine.repository;

import com.realtime.notificationengine.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEventRepository extends JpaRepository<UserEvent, String> {
    List<UserEvent> findTop20ByUserIdOrderByTimestampDesc(String userId);
}
