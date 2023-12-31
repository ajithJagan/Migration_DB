package com.flexcub.resourceplanning.invoice.entity;


import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.utils.BaseEntity;
import com.flexcub.resourceplanning.utils.StringPrefixedSequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "add_Invoice_id")
    @GenericGenerator(name = "add_Invoice_id", strategy = "com.flexcub.resourceplanning.utils.StringPrefixedSequenceGenerator", parameters = {@org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"), @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "IN-"), @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d")})
    @Column(unique = true, name = "invoice_id")
    private String id;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(targetEntity = SkillPartnerEntity.class, fetch = FetchType.EAGER)
    private SkillPartnerEntity skillPartner;

    @ManyToOne(targetEntity = InvoiceStatus.class, fetch = FetchType.EAGER)
    private InvoiceStatus invoiceStatus;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn
    private List<InvoiceData> invoiceData;

    @Column(name = "week_start_date")
    private Date weekStartDate;

    @Column(name = "week_end_date")
    private Date weekEndDate;

    @Column(columnDefinition = "text")
    private String comments="NA";
    @Column(columnDefinition = "boolean default false")
    private boolean approvedStatus;

    @Column(columnDefinition = "integer default 0")
    private Integer timesheetId;


}
