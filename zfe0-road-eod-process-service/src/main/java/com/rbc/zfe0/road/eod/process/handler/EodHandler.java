package com.rbc.zfe0.road.eod.process.handler;

import com.rbc.zfe0.road.eod.exceptions.RoadException;

import java.util.Date;
import java.util.List;

public interface EodHandler {
    List eodMainTransaction(Date lastEodDate) throws RoadException;
    void runEod() throws RoadException;
}