package com.flexcub.resourceplanning.skillowner.repository;

import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerResumeAndImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SkillOwnerResumeAndImageRepository extends JpaRepository<SkillOwnerResumeAndImage, String> {


    @Query(value = "SELECT * FROM  skill_owner_resume_and_image WHERE owner_id=?;", nativeQuery = true)
    Optional<List<SkillOwnerResumeAndImage>> findByOwnerID(int ownerId);

    @Query(value = "SELECT * FROM  skill_owner_resume_and_image WHERE owner_id=? AND type=?;", nativeQuery = true)
    Optional<SkillOwnerResumeAndImage> findByOwnerIdAndType(int ownerId, String contentType);

    @Query(value = "SELECT * FROM  skill_owner_resume_and_image WHERE owner_id=? AND resume=?;", nativeQuery = true)
    Optional<SkillOwnerResumeAndImage> findByOwnerIdAndResume(int ownerId, Boolean isResume);
    @Query(value = "SELECT * FROM  skill_owner_resume_and_image WHERE owner_id=?;", nativeQuery = true)
    Optional<SkillOwnerResumeAndImage>  findByOwnerId(int ownerId);

}
