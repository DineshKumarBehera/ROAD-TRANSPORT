package com.rbc.zfe0.road.services.transferagent.mapper;

import com.rbc.zfe0.road.services.transferagent.dto.TransferItemCountDAO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TAItemRSMapper implements RowMapper<TransferItemCountDAO> {
    public TransferItemCountDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TransferItemCountDAO(
                rs.getInt("record_count")
        );
    }
}