package com.rbc.zfe0.road.services.transferagent.repository;

import com.rbc.zfe0.road.services.transferagent.config.SQLServerSQLs;
import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentDAO;
import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentFromSctyLinkDAO;
import com.rbc.zfe0.road.services.transferagent.dto.TransferItemCountDAO;
import com.rbc.zfe0.road.services.transferagent.mapper.SecurityLinkAgentRSMapper;
import com.rbc.zfe0.road.services.transferagent.mapper.TAItemRSMapper;
import com.rbc.zfe0.road.services.transferagent.mapper.TASingleRSMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TARepository {

    //New comment
    private static Logger log = LoggerFactory.getLogger(TARepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer addAgent(TransferAgentDAO dao) {

        // Create GeneratedKeyHolder object
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        // To insert data, you need to pre-compile the SQL and set up the data yourself.
        int rowsAffected = jdbcTemplate.update(conn -> {

            // Pre-compiling SQL
            PreparedStatement preparedStatement = conn.prepareStatement(SQLServerSQLs.INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

            // Set parameters
            preparedStatement.setString(1, dao.getAGT_NM());
            preparedStatement.setString(2, dao.getADDR_BOX());
            preparedStatement.setString(3, dao.getTEL());
            preparedStatement.setString(4, dao.getFAX());
            preparedStatement.setString(5, dao.getFEE_AMT());

            preparedStatement.setDate(6, new java.sql.Date(dao.getLST_USED_DT().getTime()));
            preparedStatement.setString(7, dao.getLST_UPDT_USR_NM());
            preparedStatement.setDate(8, new java.sql.Date(dao.getLST_UPDT_DT_TM().getTime()));
            preparedStatement.setInt(9, 1);
            preparedStatement.setString(10, dao.getEMAIL_ID());

            return preparedStatement;

        }, generatedKeyHolder);


        // Get auto-incremented ID
        Integer id = generatedKeyHolder.getKey().intValue();
        return id;
    }



    public int updateAgent(TransferAgentDAO dao) {

        Object[] params = new Object[]{
                dao.getAGT_NM(),
                dao.getADDR_BOX(),
                dao.getTEL(),
                dao.getFAX(),
                dao.getFEE_AMT(),
                new java.sql.Date(dao.getLST_USED_DT().getTime()),
                dao.getLST_UPDT_USR_NM(),
                new java.sql.Date(dao.getLST_UPDT_DT_TM().getTime()),
                dao.getEMAIL_ID(),
                dao.getAGT_ID()
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.TIMESTAMP,
                Types.VARCHAR,
                Types.TIMESTAMP,
                Types.VARCHAR,
                Types.BIGINT
        };

        return jdbcTemplate.update(SQLServerSQLs.UPDATE_SQL, params, types);
    }


    public int deleteAgentById(String agentId) throws Exception {
        //all the query values
        Object[] params = new Object[]{
                Integer.parseInt(agentId)
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.BIGINT
        };

        try {

            int row = jdbcTemplate.update(SQLServerSQLs.DELETE_UPDATEACTIVEFLAG_USINGAGTID,
                    params, types);

            return row;
        } catch( Exception e ) {
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }


    public int deleteAgentBySecNumber(String secNumber) throws Exception {
        //all the query values
        //Same SecNumber is used for ADPSecNumber and DainSecNumber.
        Object[] params = new Object[]{
                secNumber,
                secNumber
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
        };

        try {

            int row = jdbcTemplate.update(SQLServerSQLs.DELETE_TASECURITYLINK,
                    params, types);

            return row;
        } catch( Exception e ) {
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }
    public List<TransferAgentDAO> getAgentById(String agentId) throws Exception {
        //all the query values
        Object[] params = new Object[]{
                Integer.parseInt(agentId)
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.BIGINT
        };

        try {
            return jdbcTemplate.query(SQLServerSQLs.GETSINGLEAGENTSQL,
                    params, types,new TASingleRSMapper());
        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }

    public List<TransferAgentDAO> getAgentsBySecurityNo(String securityNo) throws Exception {

        //Get the Agent Id from Security Link Table.
        List<TransferAgentFromSctyLinkDAO> lstAgentIdsDAO;
        List<Long> lstAgentIds = new ArrayList<>();

        lstAgentIdsDAO = getAgentNumbersBySecurityNo(securityNo);

        if (lstAgentIdsDAO != null && lstAgentIdsDAO.size() > -1) {
            for (TransferAgentFromSctyLinkDAO agent : lstAgentIdsDAO) {
                lstAgentIds.add(agent.getAGT_ID());
            }
        }

        //Once all agents are collected from Securty Link Table, now get the agents.
        if (lstAgentIds.size() > -1) {

            String inParams = String.join(",", lstAgentIds.stream().map(id -> id+"").collect(Collectors.toList()));
            List<TransferAgentDAO> agents = jdbcTemplate.query(
                    String.format(SQLServerSQLs.GETMULTIAGENTSQL,inParams), new TASingleRSMapper());


            return agents;
        } else {
            List<TransferAgentDAO> lstEmpty = new ArrayList<TransferAgentDAO>();
            return lstEmpty;
        }
    }
    public List<TransferItemCountDAO> checkTransferItemExistForAgent(String agentId) throws Exception {
        //all the query values
        Object[] params = new Object[]{
                Long.parseLong(agentId)
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.BIGINT
        };

        try {
            System.out.println("Checking ");
            return jdbcTemplate.query(SQLServerSQLs.GETTRANSFERITEMFORAGENT,
                    params, types,new TAItemRSMapper());
        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }

    public List<TransferItemCountDAO> checkTransferAgentIsInactive(String agentId) throws Exception {
        //all the query values
        Object[] params = new Object[]{
                Long.parseLong(agentId)
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.BIGINT
        };

        try {


            return jdbcTemplate.query(SQLServerSQLs.CHECKAGENTISINACTIVE,
                    params, types,new TAItemRSMapper());
        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }


    public List<TransferAgentDAO> getAgentByName(String agentName) throws Exception {
        //all the query values
        Object[] params = new Object[]{
                "%" + agentName.toLowerCase() + "%"
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.VARCHAR
        };

        try {
            return jdbcTemplate.query(SQLServerSQLs.GETAGENTBYNAMESQL,
                    params, types,new TASingleRSMapper());
        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }


    private List<TransferAgentFromSctyLinkDAO> getAgentNumbersBySecurityNo(String securityNo) throws Exception {
        //all the query values
        Object[] params = new Object[]{
                securityNo.toLowerCase(),
                securityNo.toLowerCase(),
        };

        //and their corresponding data types
        int[] types = new int[]{
                Types.VARCHAR,
                Types.VARCHAR
        };

        try {
            return jdbcTemplate.query(SQLServerSQLs.GETTRANSFERAGENTFORSECURITYNUMBER,
                    params, types,new SecurityLinkAgentRSMapper());
        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }
    }
    public List<TransferAgentDAO> getAgents(List<Integer> lstAgents) throws Exception {

        try {

            String inParams = String.join(",", lstAgents.stream().map(id -> id+"").collect(Collectors.toList()));
            return jdbcTemplate.query(
                    String.format(SQLServerSQLs.GETMULTIAGENTSQL,inParams), new TASingleRSMapper());

        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }

    }

    public List<TransferAgentDAO> getAllAgents() throws Exception {
        try {
            return jdbcTemplate.query(
                    String.format(SQLServerSQLs.GETALLAGENTSQL), new TASingleRSMapper());

        } catch( Exception e ) {
            e.printStackTrace();
            System.out.println("      Error in running Query -> {}" +  e.getMessage());
            throw new Exception(e.getLocalizedMessage());
        }

    }

    public void checkAgentReferences(Integer agentId) throws Exception {

    }
}