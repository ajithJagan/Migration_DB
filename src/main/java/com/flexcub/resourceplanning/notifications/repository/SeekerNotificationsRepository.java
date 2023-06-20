package com.flexcub.resourceplanning.notifications.repository;

import com.flexcub.resourceplanning.notifications.entity.SeekerNotificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SeekerNotificationsRepository extends JpaRepository<SeekerNotificationsEntity, Integer> {

    @Query(value = "SELECT * FROM seeker_notifications WHERE skill_seeker_entity_id = ? ORDER BY id ASC;", nativeQuery = true)
    Optional<List<SeekerNotificationsEntity>> findBySkillSeekerEntityId(int id);

    @Query(value = "SELECT * FROM seeker_notifications WHERE tax_id_business_license = ? order by date desc;", nativeQuery = true)
    Optional<List<SeekerNotificationsEntity>> findByTaxIdBusinessLicense(String taxId);

    SeekerNotificationsEntity findById(int id);

    @Query(value = "UPDATE seeker_notifications SET mark_as_read = true WHERE id = ?;", nativeQuery = true)
    Boolean updateMarkAsRead(int id);

    @Query(value = "select * from seeker_notifications WHERE tax_id_business_license = ? AND mark_as_read = false order by date desc limit 5;", nativeQuery = true)
    List<SeekerNotificationsEntity> findLastFiveNotificationByTaxId(String taxId);

    @Query(value = "SELECT * FROM seeker_notifications WHERE owner_id = ? AND job_id = ?", nativeQuery = true)
    Optional<List<SeekerNotificationsEntity>> findByOwnerIdAndJobId(int id, String jobId);

    @Query(value = "DELETE FROM public.seeker_notifications\n" +
            "WHERE date < CURRENT_DATE - interval '3 month'; ", nativeQuery = true)
    void deleteByDate();


}
