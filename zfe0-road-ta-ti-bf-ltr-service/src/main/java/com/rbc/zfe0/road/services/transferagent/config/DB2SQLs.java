package com.rbc.zfe0.road.services.transferagent.config;

import org.springframework.beans.factory.annotation.Value;

public class DB2SQLs {

    @Value("${schema.name}")
    public static String schemaName = "dzfe0";

    public static String GETSINGLEAGENTSQL = "select * from " + schemaName + ".T_TRANSFERAGENT where" +
            " activeflag=1 AND TRANSFERAGENT_ID=?";

    public static String DELETEAGENTSQL= "delete * from " + schemaName + ".T_TRANSFERAGENT where" +
            " TRANSFERAGENT_ID=?";

    public static String DELETE_UPDATEACTIVEFLAG= "update " + schemaName + ".T_TRANSFERAGENT set " +
            " activeflag=0  where TRANSFERAGENT_ID=?";

    public static String GETMULTIAGENTSQL = "select * from " + schemaName + ".T_TRANSFERAGENT where" +
            " activeflag=1 AND TRANSFERAGENT_ID IN (%s)";

    public static String GETALLAGENTSQL = "select * from " + schemaName + ".T_TRANSFERAGENT WHERE activeflag =1  ";

    public static String GETAGENTBYNAMESQL = "select * from " + schemaName + ".T_TRANSFERAGENT WHERE " +
            " activeflag=1 AND LOWER(TRANSFERAGENTNAME) LIKE ?";

    public static String GETALL_INACTIVEAGENTSQL = "select * from " + schemaName + ".T_TRANSFERAGENT WHERE activeflag =0 ";


    public static String INSERT_SQL = "INSERT INTO " + schemaName + ".T_TRANSFERAGENT (TRANSFERAGENT_ID,\n" +
            "    TRANSFERAGENTNAME,\n" +
            "    ADDRESSBOX,\n" +
            "    PHONENUMBER,\n" +
            "    FAXNUMBER,\n" +
            "    FEEAMOUNT,\n" +
            "    LASTUSEDT,\n" +
            "    LASTUPDATENAME,\n" +
            "    LASTUPDATEDT,\n" +
            "    ACTIVEFLAG,\n" +
            "    EMAIL_ID ) values (?,?,?,?,?,?,?,?,?,?,?)";

    public static String UPDATE_SQL = "UPDATE " + schemaName + ".T_TRANSFERAGENT (TRANSFERAGENT_ID,\n" +
            "    TRANSFERAGENTNAME,\n" +
            "    ADDRESSBOX,\n" +
            "    PHONENUMBER,\n" +
            "    FAXNUMBER,\n" +
            "    FEEAMOUNT,\n" +
            "    LASTUSEDT,\n" +
            "    LASTUPDATENAME,\n" +
            "    LASTUPDATEDT,\n" +
            "    ACTIVEFLAG,\n" +
            "    EMAIL_ID ) values (?,?,?,?,?,?,?,?,?,?,?)";

}
