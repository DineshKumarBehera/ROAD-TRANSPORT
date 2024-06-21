package com.rbc.zfe0.road.services.transferagent.mapper;

import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentDAO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TASingleRSMapper implements RowMapper<TransferAgentDAO> {
    public TransferAgentDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TransferAgentDAO(
                rs.getLong("AGT_ID"),
                rs.getString("AGT_NM"),
                rs.getString("ADDR_BOX"),
                rs.getString("TEL"),
                rs.getString("FAX"),
                rs.getString("FEE_AMT"),
                rs.getDate("LST_USED_DT"),
                rs.getString("LST_UPDT_USR_NM"),
                rs.getDate("LST_UPDT_DT_TM"),
                rs.getInt("ACTV_FLG_IND"),
                rs.getString("EMAIL_ID")
        );
    }
}