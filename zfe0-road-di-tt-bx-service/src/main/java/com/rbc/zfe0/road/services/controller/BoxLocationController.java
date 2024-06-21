package com.rbc.zfe0.road.services.controller;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.ApiResponse;
import com.rbc.zfe0.road.services.dto.boxlocation.BoxLocationRequest;
import com.rbc.zfe0.road.services.dto.boxlocation.BoxLocationResponse;
import com.rbc.zfe0.road.services.service.BoxLocationService;
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
@RequestMapping("/v1/boxlocation")
public class BoxLocationController {

    private final BoxLocationService boxLocationService;

    @Autowired
    public BoxLocationController(BoxLocationService boxLocationService) {
        this.boxLocationService = boxLocationService;
    }

    @GetMapping(path = "/getAllBoxLocations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> readAllBoxLocations() {
        log.info("Get all box locations");
        try {
            List<BoxLocationResponse> boxLocation = boxLocationService.getAllBoxLocations();
            return new ResponseEntity<>(boxLocation, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching all the box locations: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to get all the box location types.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody BoxLocationRequest boxLocReq) {
        log.info("Create an box location");
        try {
            boxLocationService.createBoxLocation(boxLocReq);
            return new ResponseEntity<>(ApiResponse.builder().message("Box Location is created successfully.").build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating new box location: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to create new box location type.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/update/{boxLocationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("boxLocationId") Integer boxLocationId, @RequestBody BoxLocationRequest boxLocReq) {
        log.info("Update a box location");
        try {
            boxLocationService.updateBoxLocation(boxLocationId, boxLocReq);
            return new ResponseEntity<>(ApiResponse.builder().message("Box Location is updated successfully.").build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating the box location: {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to updateEntryCode box location type.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete/{boxLocationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> delete(@PathVariable("boxLocationId") Integer boxLocationId) {
        log.info("Delete a box location");
        try {
            Map<String, String> record = boxLocationService.deleteByBoxLocationId(boxLocationId);
            if (record.containsKey(Constants.Exist)) {
                return new ResponseEntity<>(ApiResponse.builder().message(record.get(Constants.Exist)).build(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.builder().message(record.get(Constants.NotExist)).build(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Unexpected Error occurred while deleting an box location  {}", e.getMessage());
            return new ResponseEntity<>(ApiResponse.builder().error("Failed to delete box location type").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
