package com.cakesandsnacks.repository;

import com.cakesandsnacks.entity.NotificationPreference;
import com.cakesandsnacks.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// NotificationPreference Repository
@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    List<NotificationPreference> findByUserId(Long userId);
    Optional<NotificationPreference> findByUserIdAndNotificationType(Long userId, NotificationType type);
}
