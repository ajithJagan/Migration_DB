package com.flexcub.resourceplanning.skillowner.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.skillowner.dto.Client;
import com.flexcub.resourceplanning.skillowner.dto.ClientDetails;
import com.flexcub.resourceplanning.skillowner.entity.ClientEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.ClientRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillowner.service.ClientService;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    SkillOwnerRepository skillOwnerRepository;
    @Autowired
    ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Transactional
    @Override
    public ClientDetails getClient(int ownerId) {
        try {
            List<ClientEntity> bySkillOwnerEntityId = clientRepository.findBySkillOwnerEntityId(ownerId);

            ClientDetails clientDetails2 = new ClientDetails();
            if (!bySkillOwnerEntityId.isEmpty()) {
                for (ClientEntity clientEntity1 : bySkillOwnerEntityId) {
                    modelMapper.map(bySkillOwnerEntityId, Client.class);
                    BeanUtils.copyProperties(bySkillOwnerEntityId, clientEntity1, NullPropertyName.getNullPropertyNames(bySkillOwnerEntityId));
                    clientDetails2.setClient(bySkillOwnerEntityId);
                    clientDetails2.setSkillOwnerEntityId(clientEntity1.getSkillOwnerEntityId());
                }
                logger.info("ClientServiceImpl || getClient || Get all clients from the ClientEntity");
                Collections.sort(bySkillOwnerEntityId, Comparator.comparing(ClientEntity::getStartDate).reversed());
                return modelMapper.map(clientDetails2, ClientDetails.class);

            } else {
                throw new ServiceException(INVALID_OWNER_ID.getErrorCode(), INVALID_OWNER_ID.getErrorDesc());
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }

    }

    @Override
    public List<Client> insertClient(List<Client> clientList) {

        try {
            List<Client> savedClients = new ArrayList<>();

            List<ClientEntity> existingClients = clientRepository.findAllById(
                    clientList.stream().map(Client::getClientId).collect(Collectors.toList()));

            for (Client client : clientList) {
                Optional<ClientEntity> clientEntityOpt = existingClients.stream()
                        .filter(existingClient -> existingClient.getClientId() == (client.getClientId()))
                        .findFirst();

                if (clientEntityOpt.isPresent()) {
                    BeanUtils.copyProperties(client, clientEntityOpt.get(), NullPropertyName.getNullPropertyNames(client));
                    ClientEntity existingClientEntity = clientEntityOpt.get();
                    ClientEntity savedClientEntity = clientRepository.save(existingClientEntity);
                    savedClients.add(modelMapper.map(savedClientEntity, Client.class));
                } else {
                    ClientEntity newClientEntity = modelMapper.map(client, ClientEntity.class);
                    ClientEntity savedClientEntity = clientRepository.save(newClientEntity);
                    savedClients.add(modelMapper.map(savedClientEntity, Client.class));
                }
            }

            Collections.sort(savedClients, Comparator.comparing(Client::getStartDate).reversed());
            return savedClients;
        } catch (ServiceException e) {
            throw new ServiceException(CLIENT_REQUEST.getErrorCode(), CLIENT_REQUEST.getErrorDesc());
        }

    }


    @Override
    public ClientEntity updateClient(ClientEntity clientEntity) {
        logger.info("ClientServiceImpl || updateClient || client detail was updated=={}", clientEntity);
        try {
            Optional<ClientEntity> client = clientRepository.findById(clientEntity.getClientId());
            if (client.isPresent()) {
                int skillOwnerId = client.get().getSkillOwnerEntityId();
                BeanUtils.copyProperties(clientEntity, client.get(), NullPropertyName.getNullPropertyNames(clientEntity));
                client.get().setSkillOwnerEntityId(skillOwnerId);
                return clientRepository.save(client.get());
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), "Invalid Input or Null");
        }
    }

    @Override
    public void deleteClient(int clientId) {
        logger.info("ClientServiceImpl || deleteClient || Client detail was deleted by particular ClientId=={}", clientId);
        try {
            clientRepository.deleteById(clientId);
        } catch (NullPointerException e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }
}
