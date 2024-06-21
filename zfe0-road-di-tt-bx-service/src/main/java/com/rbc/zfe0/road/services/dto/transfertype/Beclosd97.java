package com.rbc.zfe0.road.services.dto.transfertype;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Beclosd97 implements Serializable {

    List<Entry> normalClose;
    List<Entry> escheatedClose;
    List<Entry> worthlessClose;
    List<Entry> rejectedClose;
    List<Entry> confiscatedClose;
}
