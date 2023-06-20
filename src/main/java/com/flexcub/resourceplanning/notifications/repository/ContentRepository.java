package com.flexcub.resourceplanning.notifications.repository;

import com.flexcub.resourceplanning.notifications.entity.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContentRepository extends JpaRepository<ContentEntity, Integer> {

    @Query(value = "Select * from content_notifications where id = 1;", nativeQuery = true)
    ContentEntity findByScheduleInterview();

    @Query(value = "Select * from content_notifications where id = 2;", nativeQuery = true)
    ContentEntity findByOwnerSlotId();

    @Query(value = "Select * from content_notifications where id = 3;", nativeQuery = true)
    ContentEntity findByAccept();

    @Query(value = "Select * from content_notifications where id = 4;", nativeQuery = true)
    ContentEntity findByNewSlot();

    @Query(value = "Select * from content_notifications where id = 5;", nativeQuery = true)
    ContentEntity findByConfirmBySeeker();

    @Query(value = "Select * from content_notifications where id = 6;", nativeQuery = true)
    ContentEntity findByShortlist();

    @Query(value = "Select * from content_notifications where id = 7;", nativeQuery = true)
    ContentEntity findByQualified();

    @Query(value = "Select * from content_notifications where id = 8;", nativeQuery = true)
    ContentEntity findByRejected();

    @Query(value = "Select * from content_notifications where id = 9;", nativeQuery = true)
    ContentEntity findByReinitiation();

    @Query(value = "Select * from content_notifications where id = 10;", nativeQuery = true)
    ContentEntity findByAutoScheduleInterview();

    @Query(value = "Select * from content_notifications where id = 11;", nativeQuery = true)
    ContentEntity findByCommonSlot();

    @Query(value = "Select * from content_notifications where id = 12;", nativeQuery = true)
    ContentEntity findByCandidateActive();

    @Query(value = "Select * from content_notifications where id = 13;", nativeQuery = true)
    ContentEntity findByCandidateInActive();

    @Query(value = "Select * from content_notifications where id =14;", nativeQuery = true)
    ContentEntity findByMSA();

    @Query(value = "Select * from content_notifications where id =15;", nativeQuery = true)
    ContentEntity findBySow();

    @Query(value = "Select * from content_notifications where id =16;", nativeQuery = true)
    ContentEntity findByPO();

//    @Query(value = "Select * from content_notifications where id = 17;", nativeQuery = true)
//    ContentEntity findByMsaInititated();
//
//    @Query(value = "Select * from content_notifications where id = 18;", nativeQuery = true)
//    ContentEntity findByMsaInWriting();
//
//    @Query(value = "Select * from content_notifications where id = 19;", nativeQuery = true)
//    ContentEntity findByMsaInProgress();
//
//    @Query(value = "Select * from content_notifications where id = 21;", nativeQuery = true)
//    ContentEntity findByPoReleased();
//
//    @Query(value = "Select * from content_notifications where id = 20;", nativeQuery = true)
//    ContentEntity findByPOInitiated();
//
//    @Query(value = "Select * from content_notifications where id = 22;", nativeQuery = true)
//    ContentEntity findBySowInitiated();
//    @Query(value = "Select * from content_notifications where id = 23;", nativeQuery = true)
//
//    ContentEntity findBySowReleased();



    @Query(value = "Select * from content_notifications where id = 17;", nativeQuery = true)
    ContentEntity findByInitiated();
    @Query(value = "Select * from content_notifications where id = 18;", nativeQuery = true)
    ContentEntity findByInWriting();

    @Query(value = "Select * from content_notifications where id = 19;", nativeQuery = true)
    ContentEntity findByInProgress();

  
    @Query(value = "Select * from content_notifications where id = 21;", nativeQuery = true)
    ContentEntity findByInvoiceApproval();

    @Query(value = "Select * from content_notifications where id = 22;", nativeQuery = true)
    ContentEntity findByInvoiceReject();

    @Query(value = "Select * from content_notifications where id = 23;", nativeQuery = true)
    ContentEntity findByInvoicePending();

    @Query(value = "Select * from content_notifications where id = 24;", nativeQuery = true)
    ContentEntity findByInvoicePaid();

   

    @Query(value = "Select * from content_notifications where id = 20;", nativeQuery = true)
    ContentEntity findByReleased();
    @Query(value = "Select * from content_notifications where id = 25;", nativeQuery = true)
    ContentEntity findByInvoiceSubmitted();
    @Query(value = "Select * from content_notifications where id = 26;", nativeQuery = true)
    ContentEntity findBySend();
    @Query(value = "Select * from content_notifications where id = 27;", nativeQuery = true)
    ContentEntity findByAccepted();
    @Query(value = "Select * from content_notifications where id = 28;", nativeQuery = true)
    ContentEntity findBySubmitted();
    @Query(value = "Select * from content_notifications where id = 29;", nativeQuery = true)
    ContentEntity findByApproved();
    @Query(value = "Select * from content_notifications where id = 30;", nativeQuery = true)
    ContentEntity findByReject();
    @Query(value = "Select * from content_notifications where id = 31;", nativeQuery = true)
    ContentEntity findByExpired();
    @Query(value = "Select * from content_notifications where id = 32;", nativeQuery = true)
    ContentEntity findByExpiringSoon();
    @Query(value = "Select * from content_notifications where id = 33;", nativeQuery = true)
    ContentEntity findByUpdated();

    @Query(value = "Select * from content_notifications where id = 34;", nativeQuery = true)
    ContentEntity findByInReview();
}
