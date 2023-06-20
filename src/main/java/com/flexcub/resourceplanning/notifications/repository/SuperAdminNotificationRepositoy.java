package com.flexcub.resourceplanning.notifications.repository;

import com.flexcub.resourceplanning.notifications.entity.SuperAdminNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SuperAdminNotificationRepositoy extends JpaRepository<SuperAdminNotifications, Integer> {

    @Query(value = "select * from super_admin_notifications where mark_as_read = false order by date desc limit 5;", nativeQuery = true)
    List<SuperAdminNotifications> findLastFiveNotification();

}
