package com.flexcub.resourceplanning.invoice.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "seeker_invoice_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SkillSeekerInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "seeker_invoice_id")
    public int seekerInvoiceId;

    @Column(name = "status")
    public int seekerInvoiceStatus;

    @Column(name = "comments")
    public String comment;

    @JoinColumn(name = "admin_invoice_id")
    private String adminInvoiceId;

}
