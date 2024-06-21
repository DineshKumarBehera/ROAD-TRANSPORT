package com.rbc.zfe0.road.services.transferagent.service;

import com.rbc.zfe0.road.services.transferagent.domain.*;
import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentDAO;
import com.rbc.zfe0.road.services.transferagent.dto.TransferItemCountDAO;
import com.rbc.zfe0.road.services.transferagent.repository.TARepository;
import com.rbc.zfe0.road.services.transferagent.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TAService {

    private static Logger log = LoggerFactory.getLogger(TAService.class);

    @Autowired
    TARepository repository;

    public TransferAgentCUDResponse addAgent(TAAddRequest request) throws Exception{

        TransferAgentDAO dao =  new TransferAgentDAO (
                0000,
                request.getAgentName(),
                request.getAddressBox(),
                request.getPhone(),
                request.getFax(),
                request.getFee(),
                DateUtil.getCurrentDateReturnDate(),
                request.getUpdatedBy(),
                DateUtil.getCurrentDateReturnDate(),
                1,
                request.getEmail());

        Integer res = repository.addAgent(dao);
        if (res > 0) {
            return new TransferAgentCUDResponse(true, res+"", "Transfer Agent added Successfully");
        }
        else
            return new TransferAgentCUDResponse(false, "0", "Error in adding Transfer Agent");
    }


    public TransferAgentCUDResponse deleteAgent(TADeleteRequest request) throws Exception {

        //Check whether there are any references in Transfer Item
        List<TransferItemCountDAO> lstAgentsItemCount;

        lstAgentsItemCount = repository.checkTransferItemExistForAgent(request.getAgentId());

        long count =0;
        if (lstAgentsItemCount != null && lstAgentsItemCount.size() > -1) {
            count = lstAgentsItemCount.get(0).getRecord_count();

            log.info("lstAgentsItemCount.get(0).getItem_count()" + count);
        }

        if (count > 0) {
            return new TransferAgentCUDResponse(false, request.getAgentId() , "Transfer Agent cannot be marked inactive because Transfer Items exists");
        }

        if (count ==0) {

            //Check whether the Transfer Agent is already inactive
            List<TransferItemCountDAO> lstAgentInActive;
            lstAgentInActive = repository.checkTransferAgentIsInactive(request.getAgentId());

            if (lstAgentInActive != null && lstAgentInActive.size() > -1) {

                log.info("Inside Second Check " + lstAgentInActive.get(0).getRecord_count());

                if (lstAgentInActive.get(0).getRecord_count() <= 0) {
                    return new TransferAgentCUDResponse(false, request.getAgentId() , "Transfer Agent is already marked inactive");
                }
            }


            int retVal = repository.deleteAgentById(request.getAgentId());

            if (retVal > 0) {
                return new TransferAgentCUDResponse(true, request.getAgentId() , "Transfer Agent marked inactive");
            }
            else {
                return new TransferAgentCUDResponse(false, request.getAgentId() , "Error occured in marking Transfer Agent inactive");
            }
        }
        else {
            return new TransferAgentCUDResponse(false, request.getAgentId() , "Error occured in marking Transfer Agent inactive");
        }
    }

    /**
     * Delete Transfer Agent with ADP Security Number or DAIN Security Number
     * @param request
     * @return
     * @throws Exception
     */
    public TransferAgentCUDResponse deleteAgentUsingSecurityNumber(TADeleteRequest request) throws Exception {

        String secNumber = request.getSecurityNumber();
        int retVal = repository.deleteAgentBySecNumber(secNumber);

        if (retVal > 0) {
            return new TransferAgentCUDResponse(true, secNumber , "Transfer Agent security link removed");
        }
        else {
            return new TransferAgentCUDResponse(false, secNumber , "Error occured in removing security link for Transfer Agent");
        }
    }

    public TransferAgentCUDResponse updateAgent(TAUpdateRequest request) throws Exception{

        TransferAgentDAO dao =  new TransferAgentDAO (
                Long.parseLong(request.getAgentId()),
                request.getAgentName(),
                request.getAddressBox(),
                request.getPhone(),
                request.getFax(),
                request.getFee(),
                DateUtil.getCurrentDateReturnDate(),
                request.getUpdatedBy(),
                DateUtil.getCurrentDateReturnDate(),
                1,
                request.getEmail());

        int res = repository.updateAgent(dao);
        if (res > 0) {
            return new TransferAgentCUDResponse(true, request.getAgentId()+"", "Transfer Agent edited Successfully");
        }
        else
            return new TransferAgentCUDResponse(false, "0", "Error in editing Transfer Agent");

    }


    public TAGetResponse getAgent(TAGetRequest request){
        try {

            String agentId = request.getAgentId();
            String agentName = request.getAgentName();
            String securityNo = request.getSecNo();

            log.info("id " + agentId + "   Name " + agentName);
            List<TransferAgentDAO> lstAgents;
            if (agentId != "" && agentId != null) {
                log.info("In id ");
                lstAgents = repository.getAgentById(agentId);
            }
            else if (agentName != "" && agentName != null) {
                log.info("In Name ");
                lstAgents = repository.getAgentByName(agentName);
            }
            else if (securityNo != "" && securityNo != null) {
                log.info("In Security Number");
                lstAgents = repository.getAgentsBySecurityNo(securityNo);
            }
            else {
                log.info("In Nothing ");
                List<TransferAgentDAO> lstEmpty = new ArrayList<>();
                return new TAGetResponse(false, -1, lstEmpty);
            }

            log.info("reached here " );
            return new TAGetResponse(true, lstAgents.size(), lstAgents);
        }
        catch (Exception e) {
            e.printStackTrace();
            List<TransferAgentDAO> lstEmpty = new ArrayList<>();
            return new TAGetResponse(false, -1, lstEmpty);
        }
    }
    private TAGetResponse getAgentById(String agentId){
        try {
            List<TransferAgentDAO> lstAgents = repository.getAgentById(agentId);
            return new TAGetResponse(true, lstAgents.size(), lstAgents);
        }
        catch (Exception e) {
            List<TransferAgentDAO> lstEmpty = new ArrayList<>();
            return new TAGetResponse(false, -1, lstEmpty);
        }
    }

    private TAGetResponse getAgentByName(String agentName){
        try {
            List<TransferAgentDAO> lstAgents = repository.getAgentByName(agentName);
            return new TAGetResponse(true, lstAgents.size(), lstAgents);
        }
        catch (Exception e) {
            List<TransferAgentDAO> lstEmpty = new ArrayList<>();
            return new TAGetResponse(false, -1, lstEmpty);
        }
    }


    public TAGetResponse getAgents(TAGetRequest request){
        try {

            List<Integer> agents = request.getAgentIdsList();
            List<TransferAgentDAO> lstAgents = repository.getAgents(agents);


            return new TAGetResponse(true, lstAgents.size(), lstAgents);
        }
        catch (Exception e) {
            List<TransferAgentDAO> lstEmpty = new ArrayList<>();
            return new TAGetResponse(false, -1, lstEmpty);
        }
    }

    public TAGetResponse getAllAgents(){
        try {
            List<TransferAgentDAO> lstAgents = repository.getAllAgents();
            return new TAGetResponse(true, lstAgents.size(), lstAgents);
        }
        catch (Exception e) {
            List<TransferAgentDAO> lstEmpty = new ArrayList<>();
            return new TAGetResponse(false, -1, lstEmpty);
        }
    }


    public TAGetResponse getAgentsByFilter(TAFilterRequest request){
        try {

            List<Integer> agents = List.of(2392, 5152);
            List<TransferAgentDAO> lstAgents = repository.getAgents(agents);


            return new TAGetResponse(true, lstAgents.size(), lstAgents);
        }
        catch (Exception e) {
            List<TransferAgentDAO> lstEmpty = new ArrayList<>();
            return new TAGetResponse(false, -1, lstEmpty);
        }
    }




}