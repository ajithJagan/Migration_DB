package com.flexcub.resourceplanning.notifications.repository;

import com.flexcub.resourceplanning.notifications.entity.PartnerNotificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PartnerNotificationsRepository extends JpaRepository<PartnerNotificationsEntity, Integer> {

    @Query(value = "SELECT * FROM partner_notifications WHERE skill_partner_entity_id = ? ", nativeQuery = true)
    Optional<List<PartnerNotificationsEntity>> findBySkillPartnerEntityId(int id);

    PartnerNotificationsEntity findById(int id);

    @Query(value = "UPDATE partner_notifications SET mark_as_read = true WHERE id = ?;", nativeQuery = true)
    PartnerNotificationsEntity updateMarkAsRead(int id);

    @Query(value = "select * from partner_notifications WHERE  skill_partner_entity_id = ? AND mark_as_read = false order by date desc limit 5;", nativeQuery = true)
    List<PartnerNotificationsEntity> findLastFiveNotification(int partnerId);

    @Query(value = "select * from partner_notifications WHERE owner_id = ? AND job_id = ?;", nativeQuery = true)
    Optional<List<PartnerNotificationsEntity>> findByOwnerIdAndJobId(int ownerId, String jobId);

    @Query(value = "DELETE FROM public.partner_notifications\n" +
            "WHERE date < CURRENT_DATE - interval '3 month'; ", nativeQuery = true)
    void deleteByDate();


}
