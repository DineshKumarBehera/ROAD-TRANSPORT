package com.rbc.zfe0.road.services.transferagent.mapper;

import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentDAO;
import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentFromSctyLinkDAO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SecurityLinkAgentRSMapper implements RowMapper<TransferAgentFromSctyLinkDAO> {
    public TransferAgentFromSctyLinkDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TransferAgentFromSctyLinkDAO(
                rs.getLong("AGT_ID")
        );
    }
}