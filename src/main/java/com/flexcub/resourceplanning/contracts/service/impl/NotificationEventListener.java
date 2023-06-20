package com.flexcub.resourceplanning.contracts.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.notifications.entity.ContentEntity;
import com.flexcub.resourceplanning.notifications.entity.PartnerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.entity.SeekerNotificationsEntity;
import com.flexcub.resourceplanning.notifications.repository.ContentRepository;
import com.flexcub.resourceplanning.notifications.repository.PartnerNotificationsRepository;
import com.flexcub.resourceplanning.notifications.repository.SeekerNotificationsRepository;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerEntity;
import com.flexcub.resourceplanning.skillseeker.repository.ContractStatusRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.NOTIFICATION_FAILED_TO_SEND;

@Component
public class NotificationEventListener implements ApplicationListener<NotificationEvent> {
    @Autowired
    SkillPartnerRepository skillPartnerRepository;

    @Autowired
    SkillSeekerRepository skillSeekerRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    ContractStatusRepository contractStatusRepository;
    @Autowired
    PartnerNotificationsRepository partnerNotificationsRepository;

    @Autowired
    SeekerNotificationsRepository seekerNotificationsRepository;
    @Value("${formatter.dateType}")
    private String dateType;

    @Override
    public void onApplicationEvent(NotificationEvent notificationEvent) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateType);
            LocalDateTime now = LocalDateTime.now();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Future<?> task1 = executor.submit(() -> {
                List<SkillPartnerEntity> skillPartnerEntities1 = skillPartnerRepository.findbyMsaEndDate();
                for (SkillPartnerEntity skillPartnerEntity : skillPartnerEntities1) {
                    ContentEntity content = contentRepository.findByExpired();
                    PartnerNotificationsEntity partnerNotifications = new PartnerNotificationsEntity();
                    partnerNotifications.setContent("Your MSA is Expiring by today -  " + " " + formatter.format(now) + ". Please Make a Renewal. ");
                    partnerNotifications.setSkillPartnerEntityId(skillPartnerEntity.getSkillPartnerId());
                    partnerNotifications.setContentId(content.getId());
                    partnerNotifications.setTitle("MSA - " + content.getTitle());
                    partnerNotifications.setDate(new Date());
                    partnerNotifications.setContentObj(content);
                    partnerNotifications.setMarkAsRead(false);
                    skillPartnerEntity.setMsaStatusId(contractStatusRepository.findById(10).get());
                    skillPartnerRepository.save(skillPartnerEntity);
                    partnerNotificationsRepository.save(partnerNotifications);
                }
            });
            Future<?> task2 = executor.submit(() -> {
                List<SkillSeekerEntity> skillSeekerEntities = skillSeekerRepository.findbyMsaEndDate();
                for (SkillSeekerEntity skillSeekerEntity : skillSeekerEntities) {
                    ContentEntity content = contentRepository.findByExpired();
                    SeekerNotificationsEntity seekerNotification = new SeekerNotificationsEntity();
                    seekerNotification.setContent("Your MSA is Expiring by today -  " + " " + formatter.format(now) + ". Please Make a Renewal. ");
                    seekerNotification.setSkillSeekerEntityId(skillSeekerEntity.getId());
                    seekerNotification.setContentId(content.getId());
                    seekerNotification.setTitle("MSA - " + content.getTitle());
                    seekerNotification.setDate(new Date());
                    seekerNotification.setContentObj(content);
                    seekerNotification.setMarkAsRead(false);
                    skillSeekerEntity.setMsaStatusId(contractStatusRepository.findById(10).get());
                    skillSeekerRepository.save(skillSeekerEntity);
                    seekerNotificationsRepository.save(seekerNotification);
                }
            });

            Future<?> task3 = executor.submit(() -> {
                List<SkillPartnerEntity> skillPartnerEntities = skillPartnerRepository.findByMsaForExpiringSoon();
                for (SkillPartnerEntity skillPartnerEntity : skillPartnerEntities) {
                    ContentEntity content = contentRepository.findByExpiringSoon();
                    PartnerNotificationsEntity partnerNotifications = new PartnerNotificationsEntity();
                    partnerNotifications.setContent("Your MSA is set to expire in three months on -  " + " " + skillPartnerEntity.getMsaEndDate() + ". Please Make a Renewal. ");
                    partnerNotifications.setSkillPartnerEntityId(skillPartnerEntity.getSkillPartnerId());
                    partnerNotifications.setContentId(content.getId());
                    partnerNotifications.setTitle("MSA - " + content.getTitle());
                    partnerNotifications.setDate(new Date());
                    partnerNotifications.setContentObj(content);
                    partnerNotifications.setMarkAsRead(false);
                    skillPartnerEntity.setMsaStatusId(contractStatusRepository.findById(11).get());
                    skillPartnerRepository.save(skillPartnerEntity);
                    partnerNotificationsRepository.save(partnerNotifications);
                }
            });
            Future<?> task4 = executor.submit(() -> {
                List<SkillSeekerEntity> skillSeekerEntities = skillSeekerRepository.findByMsaForExpiringSoon();
                for (SkillSeekerEntity skillSeekerEntity : skillSeekerEntities) {
                    ContentEntity content = contentRepository.findByExpiringSoon();
                    SeekerNotificationsEntity seekerNotifications = new SeekerNotificationsEntity();
                    seekerNotifications.setContent("Your MSA is set to expire in one months on -  " + " " + formatter.format(now) + ". Please Make a Renewal. ");
                    seekerNotifications.setSkillSeekerEntityId(skillSeekerEntity.getId());
                    seekerNotifications.setContentId(content.getId());
                    seekerNotifications.setTitle("MSA - " + content.getTitle());
                    seekerNotifications.setDate(new Date());
                    seekerNotifications.setContentObj(content);
                    seekerNotifications.setMarkAsRead(false);
                    skillSeekerEntity.setMsaStatusId(contractStatusRepository.findById(11).get());
                    skillSeekerRepository.save(skillSeekerEntity);
                    seekerNotificationsRepository.save(seekerNotifications);
                }
            });
            task1.get();
            task2.get();
            task3.get();
            task4.get();
            executor.shutdown();
        } catch (Exception e) {
            throw new ServiceException(NOTIFICATION_FAILED_TO_SEND.getErrorCode(), NOTIFICATION_FAILED_TO_SEND.getErrorDesc());
        }
    }
}

