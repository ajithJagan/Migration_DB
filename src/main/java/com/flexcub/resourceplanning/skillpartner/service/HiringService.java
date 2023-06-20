package com.flexcub.resourceplanning.skillpartner.service;

import com.flexcub.resourceplanning.skillpartner.entity.HiringEntity;

import java.util.List;

public interface HiringService {
    List<HiringEntity> getData();

    HiringEntity insertData(HiringEntity hiringEntity);

    HiringEntity updateData(HiringEntity update);

    void deleteData(int id);
}
