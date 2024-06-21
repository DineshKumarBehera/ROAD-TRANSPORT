package com.rbc.zfe0.road.eod.helper;

import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Component
@Slf4j
public class MFServiceHelper {

    @Autowired
    @Qualifier("mainframeDataSource")
    DataSource mainframeDataSource;

    @Value("${rbc.road.mainframe-datasource.schema}")
    private String mainframeDataSourceSchema;

    //get the connection
    public java.sql.Connection sqlConnect() throws Exception {
        Connection conn = null;
        try {
            conn = mainframeDataSource.getConnection();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return conn;
    }

    /**
     * Check if the given account is frozen.
     * @param brCode
     * @param accNumber
     * @return
     * @throws ServiceLinkException
     */
    public boolean isAccountFrozen(String brCode, String accNumber)
            throws ServiceLinkException {
        boolean status = false;
        Connection conn = null;
        String account = accNumber.substring(3, accNumber.length());
        CallableStatement cstmt = null;
        try {
            conn = sqlConnect();
            cstmt = conn.prepareCall("{call " + mainframeDataSourceSchema + ".RDP0200(?,?,?,?,?,?,?)}");
            cstmt.setString(1, brCode);
            cstmt.setString(2, account);
            cstmt.registerOutParameter(3, Types.CHAR);
            cstmt.registerOutParameter(4, Types.DECIMAL);
            cstmt.registerOutParameter(5, Types.CHAR);
            cstmt.registerOutParameter(6, Types.DECIMAL);
            cstmt.registerOutParameter(7, Types.VARCHAR);
            cstmt.execute();
            if (cstmt.getString(3).equalsIgnoreCase("Y")) {
                status = true;
            }
        } catch (Throwable t) {
            throw new ServiceLinkException(t);
        } finally {
            try {
                if (cstmt != null) cstmt.close();
            } catch (Throwable ignore) {
            }
            try {
                if (conn != null) conn.close();
            } catch (Throwable ignore) {
            }
        }
        return status;
    }
}
