package com.flexcub.resourceplanning.invoice.entity;


import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "admin_invoice_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminInvoiceData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private int id;

    @ManyToOne(targetEntity = SkillSeekerProjectEntity.class, fetch = FetchType.EAGER)
    private SkillSeekerProjectEntity skillSeekerProjectId;

    @Column
    private int amount;

    @ManyToOne(targetEntity = SkillOwnerEntity.class, fetch = FetchType.EAGER)
    private SkillOwnerEntity ownerId;

    @Column
    private int totalHours;

    @ManyToOne(targetEntity = InvoiceStatus.class, fetch = FetchType.EAGER)
    private InvoiceStatus invoiceStatus;

    @ManyToOne(targetEntity = SkillSeekerEntity.class, fetch = FetchType.EAGER)
    private SkillSeekerEntity skillSeeker;



}