package com.flexcub.resourceplanning.skillseeker.repository;

import com.flexcub.resourceplanning.skillseeker.entity.SeekerAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeekerAccessRepository extends JpaRepository<SeekerAccessEntity, Integer> {
    List<SeekerAccessEntity> findBySubRoles(int subroles);

    @Query(value = "SELECT * FROM public.seeker_access WHERE tax_id_business_license = ? AND sub_role_id=?", nativeQuery = true)
    List<SeekerAccessEntity> findByTaxIdAndSubRole(String taxId, int subRole);

    @Query(value = "SELECT * FROM public.seeker_access WHERE tax_id_business_license = ?", nativeQuery = true)
    List<SeekerAccessEntity> findByTaxId(String taxId);
}
