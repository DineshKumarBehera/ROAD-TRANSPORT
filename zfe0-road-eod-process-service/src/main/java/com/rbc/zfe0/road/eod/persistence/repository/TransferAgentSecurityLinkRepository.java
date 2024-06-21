package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.TransferAgentSecurityLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.persistence.entity.TransferAgentSecurityLink;
import com.rbc.zfe0.road.eod.persistence.entity.TransferAgent;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferAgentSecurityLinkRepository extends JpaRepository<TransferAgentSecurityLink, Integer> {

    @Query(value="Select\n" +
            "    sl\n" +
            "from\n" +
            "    TransferAgentSecurityLink sl\n" +
            "where\n" +
            "    sl.transferAgentId in(\n" +
            "        select\n" +
            "            ta.transferAgentId\n" +
            "        from\n" +
            "            TransferAgent ta\n" +
            "        where\n" +
            "            ta.transferAgentId not in (\n" +
            "                select\n" +
            "                    distinct ti.transferAgentId\n" +
            "                from\n" +
            "                    TransferItem ti\n" +
            "                where\n" +
            "                   ti.transferAgentId <> 0\n" +
            "            )\n" +
            "            and ta.transferAgentId in (\n" +
            "                select\n" +
            "                    distinct tasl.transferAgentId\n" +
            "                from\n" +
            "                    TransferAgentSecurityLink tasl\n" +
            "            )\n" +
            "            and ta.lastUseDt < ?1\n" +
            "    )")
    List<TransferAgentSecurityLink> deleteLastUsedSecurityLink(Date lastUsedDt);

    @Query(value="Select\n" +
            "    sl\n" +
            "from\n" +
            "    TransferAgentSecurityLink sl\n" +
            "where\n" +
            "    sl.transferAgentId in(\n" +
            "        select\n" +
            "            ta.transferAgentId\n" +
            "        from\n" +
            "            TransferAgent ta\n" +
            "        where\n" +
            "            ta.transferAgentId not in (\n" +
            "                select\n" +
            "                    distinct ti.transferAgentId\n" +
            "                from\n" +
            "                    TransferItem ti\n" +
            "                where\n" +
            "                   ti.transferAgentId <> 0\n" +
            "            )\n" +
            "            and ta.transferAgentId in (\n" +
            "                select\n" +
            "                    distinct tasl.transferAgentId from TransferAgentSecurityLink tasl\n" +
            "            )\n" +
            "            and ta.lastUpdateDt < ?1\n" +
            "    )")
    List<TransferAgentSecurityLink> deleteLastUpdateSecurityLink(Date lastUpdatedDt);


}
