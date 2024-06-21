package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.boxlocation.BoxLocationRequest;
import com.rbc.zfe0.road.services.dto.boxlocation.BoxLocationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BoxLocationService {
    public List<BoxLocationResponse> getAllBoxLocations();

    public void createBoxLocation(BoxLocationRequest boxLocReq);

    public void updateBoxLocation(Integer boxId, BoxLocationRequest boxLocReq);

    public Map<String, String> deleteByBoxLocationId(Integer boxLocationId);
}

