package com.rbc.zfe0.road.services.transferagent.controller;

import com.rbc.zfe0.road.services.transferagent.domain.*;
import com.rbc.zfe0.road.services.transferagent.service.TAService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/transferagent")
@Schema(name = "Transfer Agent")
public class TAController {

    @Autowired
    TAService service;

    @PostMapping("/agent/add")
    public ResponseEntity<TransferAgentCUDResponse> addAgent(@RequestBody TAAddRequest request) throws Exception{
        log.info("In add");
        TransferAgentCUDResponse response =  service.addAgent(request);
        return new ResponseEntity<TransferAgentCUDResponse>(response, new HttpHeaders(), HttpStatus.OK);

    }

    @PostMapping("/agent/getAgent")
    public ResponseEntity<TAGetResponse> getAgent(@RequestBody TAGetRequest request) {
        log.info("In select");

        TAGetResponse response = service.getAgent(request);
        return new ResponseEntity<TAGetResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/agent/getAgents")
    public ResponseEntity<TAGetResponse> getAgents(@RequestBody TAGetRequest request) {
        log.info("In Multi select");

        TAGetResponse response = service.getAgents(request);
        return new ResponseEntity<TAGetResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }

    //Not used.
    @GetMapping("/agent/filter/getAgents")
    public ResponseEntity<TAGetResponse> getFilterAgents(@RequestBody TAFilterRequest request) {
        log.info("In select");

        TAGetResponse response = service.getAgentsByFilter(request);
        return new ResponseEntity<TAGetResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }
    //End of not used


    @GetMapping("/agent/getAllAgents")
    public ResponseEntity<TAGetResponse> getAllAgent() {
        log.info("In select");

        TAGetResponse response = service.getAllAgents();
        return new ResponseEntity<TAGetResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping("/agent/delete")
    public ResponseEntity<TransferAgentCUDResponse> deleteAgent(@RequestBody TADeleteRequest request) throws Exception{
        log.info("In delete");
        TransferAgentCUDResponse response =  service.deleteAgent(request);

        return new ResponseEntity<TransferAgentCUDResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/agent/deleteUsingSecurityNumber")
    public ResponseEntity<TransferAgentCUDResponse> deleteAgentUsingSecNumber(@RequestBody TADeleteRequest request) throws Exception{
        log.info("In delete By Security Number");
        TransferAgentCUDResponse response =  service.deleteAgentUsingSecurityNumber(request);

        return new ResponseEntity<TransferAgentCUDResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/agent/update")
    public ResponseEntity<TransferAgentCUDResponse> updateAgent(@RequestBody TAUpdateRequest request) throws Exception{
        log.info("In update");
        TransferAgentCUDResponse response =  service.updateAgent(request);

        return new ResponseEntity<TransferAgentCUDResponse>(response, new HttpHeaders(), HttpStatus.OK);
    }

}
