package com.flexcub.resourceplanning.skillseeker.entity;

import com.flexcub.resourceplanning.utils.StringPrefixedSequenceGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;

@Entity
@Data
@Table(name = "seeker_access")
@Getter
@Setter
public class SeekerAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "add_roleId")
    @GenericGenerator(name = "add_roleId", strategy = "com.flexcub.resourceplanning.utils.StringPrefixedSequenceGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "RID-"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")})
    @Column(name = "access_Id", unique = true)
    private String accessId;

    @Column
    private String taxIdBusinessLicense;

    @OneToOne
    @JoinColumn(name = "sub_role_id")
    private SubRoles subRoles;

    @OneToOne
    @JoinColumn
    private SeekerModulesEntity seekerModulesEntity;

    @Column
    private boolean isActive;

}
