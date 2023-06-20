package com.flexcub.resourceplanning.notifications.repository;

import com.flexcub.resourceplanning.notifications.entity.OwnerNotificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface OwnerNotificationsRepository extends JpaRepository<OwnerNotificationsEntity, Integer> {

    @Query(value = "SELECT * FROM owner_notifications WHERE skill_owner_entity_id = ?", nativeQuery = true)
    Optional<List<OwnerNotificationsEntity>> findBySkillOwnerEntityId(int id);

    OwnerNotificationsEntity findById(int id);

    @Query(value = "UPDATE owner_notifications SET mark_as_read = true WHERE id = ?;", nativeQuery = true)
    OwnerNotificationsEntity updateMarkAsRead(int id);

    Optional<List<OwnerNotificationsEntity>> findByJobId(String jobId);

    @Query(value = "select * from owner_notifications WHERE skill_owner_entity_id = ? AND mark_as_read = false order by date desc limit 5;", nativeQuery = true)
    List<OwnerNotificationsEntity> findLastFiveNotification(int skillOwner);

    @Query(value = "select * from owner_notifications WHERE skill_owner_entity_id = ? AND job_id=?; ", nativeQuery = true)
    List<OwnerNotificationsEntity> findBySkillOwnerEntityIdAndJobId(int ownerId, String jobId);

    @Query(value = "DELETE FROM public.owner_notifications\n" +
            "WHERE date < CURRENT_DATE - interval '3 month'; ", nativeQuery = true)
    void deleteByDate();


}
