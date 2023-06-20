package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.Client;
import com.flexcub.resourceplanning.skillowner.dto.ClientDetails;
import com.flexcub.resourceplanning.skillowner.entity.ClientEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.ClientRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.impl.ClientServiceImpl;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ClientServiceImpl.class)
class ClientServiceImplTest {
    @MockBean
    ClientRepository clientRepository;

    @MockBean
    SkillOwnerRepository skillOwnerRepository;
    @MockBean
    ModelMapper modelMapper;
    @Autowired
    ClientServiceImpl clientService;

    ClientEntity clientEntity = new ClientEntity();
    SkillPartnerEntity skillPartnerEntity = new SkillPartnerEntity();
    List<ClientEntity> clientEntities = new ArrayList<>();
    SkillOwnerEntity skillOwnerEntity = new SkillOwnerEntity();
    Client client = new Client();
    List<Client> clients = new ArrayList<>();
    ClientDetails clientDetails = new ClientDetails();

    @BeforeEach
    public void setup() {

        skillPartnerEntity.setSkillPartnerId(1);
        skillOwnerEntity.setSkillOwnerEntityId(1);
        skillOwnerEntity.setSkillPartnerEntity(skillPartnerEntity);
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

        clientEntity.setClientId(1);
        clientEntity.setEmployerName("Java");
        clientEntity.setJobTitle("Java Developer");
        clientEntity.setDepartment("Banking");
        clientEntity.setLocation("Alaska");
        clientEntity.setProjectDescription("web designing");
        clientEntity.setSkillOwnerEntityId(skillOwnerEntity.getSkillOwnerEntityId());
        clientEntity.setProject("Online Documentation");
        clientEntity.setCurrentEmployer(true);
        clientEntity.setStartDate(new Date(2022 - 11 - 12));
        clientEntity.setEndDate(new Date(2022 - 12 - 12));

        clientEntities.add(clientEntity);

        client.setClientId(1);
        client.setEmployerName("Java");
        client.setSkillOwnerEntityId(skillOwnerEntity.getSkillOwnerEntityId());
        client.setJobTitle("Java Developer");
        client.setDepartment("Banking");
        client.setLocation("Alaska");
        client.setProject("Online Documentation");
        client.setCurrentEmployer(true);
        client.setStartDate(new Date(2023 - 04 - 01));
        client.setEndDate(new Date(2023 - 12 - 12));
        client.setProjectDescription("web Designing");
        clients.add(client);


        clientDetails.setSkillOwnerEntityId(skillOwnerEntity.getSkillOwnerEntityId());
        clientDetails.setClient(clientEntities);
    }

    @Test
    void getClientTest() {
        when(clientRepository.findBySkillOwnerEntityId(Mockito.anyInt())).thenReturn(clientEntities);
        when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(clientDetails);
        assertEquals(clientDetails, clientService.getClient(skillOwnerEntity.getSkillOwnerEntityId()));
    }

    @Test
    void insertClientTest() {
        when(clientRepository.findAllById(Mockito.anyCollection())).thenReturn(clientEntities);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
        when(modelMapper.map(clientEntity, Client.class)).thenReturn(client);
        assertEquals(clients,clientService.insertClient(clients));
    }

    @Test
    void updateClientTest() {
        when(clientRepository.findById(clientEntity.getClientId())).thenReturn(Optional.ofNullable(clientEntity));
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);
        assertThat(clientService.updateClient(clientEntity)).isEqualTo(clientEntity);
    }

    @Test
    void deleteClientTest() {
        clientService.deleteClient(1);
        Mockito.verify(clientRepository, times(1)).deleteById(1);
    }
}
