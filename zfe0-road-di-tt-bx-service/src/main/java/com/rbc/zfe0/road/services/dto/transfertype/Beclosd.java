package com.rbc.zfe0.road.services.dto.transfertype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beclosd implements Serializable {

    List<Entry> normalClose;
    List<Entry> escheatedClose;
    List<Entry> worthlessClose;
    List<Entry> rejectedClose;
    List<Entry> confiscatedClose;
}
