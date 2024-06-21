package com.rbc.zfe0.road.services.controller;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.ApiResponse;
import com.rbc.zfe0.road.services.dto.entry.EntryCodeRequest;
import com.rbc.zfe0.road.services.dto.entry.EntryCodeResponse;
import com.rbc.zfe0.road.services.service.EntryCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/entrycode")
public class EntryCodeController {

    private final EntryCodeService entryCodeService;

    @Autowired
    public EntryCodeController(EntryCodeService entryCodeService) {
        this.entryCodeService = entryCodeService;
    }

    @GetMapping(path = "/getAllEntryCodes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> readAllEntryCodes() {
        log.info("Get all entry codes");
        try {
            List<EntryCodeResponse> entryCodes = entryCodeService.getAllEntryCodes();
            return new ResponseEntity<>(entryCodes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all the entry codes: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to get all the entry code types").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody EntryCodeRequest entryCodeReq) {
        log.info("Create an entry code");
        try {
            entryCodeService.createEntryCode(entryCodeReq);
            return new ResponseEntity<>(ApiResponse.builder().message("Entry Code is created successfully.").build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating new entry code: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to create new entry code type.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/update/{entryCodeId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("entryCodeId") Integer entryCodeId, @RequestBody EntryCodeRequest entryCodeReq) {
        log.info("Update an entry code");
        try {
            entryCodeService.updateEntryCode(entryCodeId, entryCodeReq);
            return new ResponseEntity<>(ApiResponse.builder().message("Entry Code is updated successfully.").build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating the entry code: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to updateEntryCode entry code type.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete/{entryCodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> delete(@PathVariable("entryCodeId") Integer entryCodeId) {
        log.info("Delete an entry code");
        try {
            Map<String, String> record = entryCodeService.deleteByEntryCodeId(entryCodeId);
            if (record.containsKey(Constants.Exist)) {
                return new ResponseEntity<>(ApiResponse.builder().message(record.get(Constants.Exist)).build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.builder().message(record.get(Constants.NotExist)).build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occurred while deleting an entry code  {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to delete entry code type.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
