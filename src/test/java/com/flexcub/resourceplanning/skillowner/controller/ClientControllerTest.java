package com.flexcub.resourceplanning.skillowner.controller;

import com.flexcub.resourceplanning.skillowner.dto.Client;
import com.flexcub.resourceplanning.skillowner.dto.ClientDetails;
import com.flexcub.resourceplanning.skillowner.entity.ClientEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.service.impl.ClientServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = ClientController.class)
class ClientControllerTest {


    @Autowired
    ClientController clientController;

    @MockBean
    ClientServiceImpl clientService;
    ClientEntity clientEntity = new ClientEntity();
    Client client=new Client();
    SkillOwnerEntity skillOwnerEntity=new SkillOwnerEntity();
    ClientDetails clientDetails=new ClientDetails();
    List<Client> clients=new ArrayList<>();
    List<ClientEntity> clientEntities = new ArrayList<ClientEntity>();


    @BeforeEach
    public void setup() {
        clientEntity.setClientId(1);
        clientEntity.setEmployerName("Java");

        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setAddress("Salem");
        skillOwnerEntity.setCity("Salem");
        skillOwnerEntity.setFirstName("Ajith");
        skillOwnerEntity.setLastName("Kumar");
        skillOwnerEntity.setLinkedIn("linkdn");
        skillOwnerEntity.setPhoneNumber("9087654321");
        skillOwnerEntity.setPrimaryEmail("ajithashok2530@gmail.com");
        skillOwnerEntity.setRateCard(45);
        skillOwnerEntity.setState("Tamilnadu");
        skillOwnerEntity.setAccountStatus(true);
    }

    @Test
    void getSkillOwnerIdTest() {
        Mockito.when((clientService.getClient(skillOwnerEntity.getSkillOwnerEntityId()))).thenReturn(clientDetails);
        assertEquals(200, clientController.getSkillOwnerId(1).getStatusCodeValue());
    }

    @Test
    void insertClientTest() {

        Mockito.when(clientService.insertClient(clients)).thenReturn(clients);
        assertEquals(200, clientController.insertClient(clients).getStatusCodeValue());

    }

    @Test
    void updateClientTest() {
        Mockito.when(clientService.updateClient(clientEntity)).thenReturn(clientEntity);
        assertEquals(200, clientController.updateClient(clientEntity).getStatusCodeValue());

    }

    @Test
    void deleteClientTest() throws Exception {
        clientController.deleteClient(1);
        clientController.deleteClient(2);
        Mockito.verify(clientService, times(2)).deleteClient(Mockito.anyInt());
    }

}