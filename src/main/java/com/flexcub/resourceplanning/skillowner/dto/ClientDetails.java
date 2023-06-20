package com.flexcub.resourceplanning.skillowner.dto;

import com.flexcub.resourceplanning.skillowner.entity.ClientEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientDetails {

    private int skillOwnerEntityId;
    private List<ClientEntity> client;
}
