package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerTimeSheetRepository extends JpaRepository<OwnerTimeSheetEntity, Integer> {
    Optional<OwnerTimeSheetEntity> findByTimeSheetId(int timeSheetId);

    @Query(value = "SELECT * FROM  skill_owner_timesheet WHERE project_id=?;", nativeQuery = true)
    Optional<OwnerTimeSheetEntity> findByProjectId(int projectId);

    @Query(value = "SELECT *\n" +
            "\tFROM public.skill_owner_timeSheet\n" +
            "\tWHERE start_date=? AND skill_owner_id=?;", nativeQuery = true)
    Optional<List<OwnerTimeSheetEntity>> findByStartDateAndSkillOwnerEntity(Date startDate, int ownerId);


    @Query(value = "SELECT  * FROM skill_owner_timesheet WHERE project_id=? AND start_date>=? AND end_date<=? ;", nativeQuery = true)
    List<OwnerTimeSheetEntity> findByProjectIdStartDateEndDate(int id, Date startDate, Date endDate);

    @Query(value = "SELECT * FROM public.skill_owner_timesheet Where skill_owner_id=?;", nativeQuery = true)
    Optional<List<OwnerTimeSheetEntity>> findByTimesheetOwnerId(int ownerId);

    @Query(value = "SELECT * FROM public.skill_owner_timesheet Where skill_owner_id=?;", nativeQuery = true)
    Optional<List<OwnerTimeSheetEntity>> findByOwnerId(int ownerId);

    @Query(value = "Select * from skill_owner_timesheet where skill_owner_timesheet.start_date = ? and skill_owner_timesheet.end_date = ? and skill_owner_id = ?;", nativeQuery = true)
    Optional<List<OwnerTimeSheetEntity>> findByStartDateAndEndDate(Date startDate, Date endDate, int ownerId);

    @Query(value = "Select * from skill_owner_timesheet where skill_owner_timesheet.start_date >= ? and skill_owner_timesheet.end_date <= ? and skill_owner_id = ? and invoice_generated = false;", nativeQuery = true)
    Optional<List<OwnerTimeSheetEntity>> findByStartDateAndEndDateAndOwnerId(Date startDate, Date endDate, int ownerId);

//    Optional<OwnerTimeSheetEntity> findByStartDateAndTask(Date startDate, String task);

    @Query(value = "Select * from skill_owner_timesheet , (SELECT skill_owner.id\n" +
            "FROM skill_owner\n" +
            "WHERE skill_owner.skill_partner_id=?)as a\n" +
            "where skill_owner_timesheet.skill_owner_id=a.id and skill_owner_timesheet.start_date>=? and skill_owner_timesheet.end_date<=? and invoice_generated = false ;\n", nativeQuery = true)
    List<OwnerTimeSheetEntity> findByPartnerIdAndIdStartDateEndDate(int partnerId, Date startDate, Date endDate);

    @Query(value = "select * from skill_owner_timesheet where project_id=? and skill_seeker_id=?and invoice_generated=true ;",nativeQuery = true)
    List<OwnerTimeSheetEntity> findByProjectIdandSeekerId(int projectId, int seekerId);
}
