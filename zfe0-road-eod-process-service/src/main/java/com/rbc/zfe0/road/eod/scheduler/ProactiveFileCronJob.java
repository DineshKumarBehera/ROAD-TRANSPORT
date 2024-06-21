package com.rbc.zfe0.road.eod.scheduler;

import com.rbc.zfe0.road.eod.process.proactive.ProactiveFileService;
import com.rbc.zfe0.road.eod.utils.HolidayUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

@Component
@Slf4j
@EnableScheduling
public class ProactiveFileCronJob {

    @Value("${rbc.road.scheduler.proactive.run-proactive-scheduler}")
    private Boolean runScheduler;

    @Autowired
    ProactiveFileService proactiveHandler;

    @Autowired
    HolidayUtil holidayUtil;

    @Scheduled(cron = "${rbc.road.scheduler.proactive.cron-expression:}",
            zone = "${rbc.road.scheduler.proactive.time-zone:}")
    @Transactional
    public void runProactiveFileGenerationJob() {

        //Run only on Week days excluding RBC public holidays
        if (!holidayUtil.isHoliday()) {
            if (runScheduler) {
                try {
                    log.info("Proactive PROCESSING: Starting Proactive processing {}", new Date());
                    proactiveHandler.generateOTTFullStrippedFile("OTT ");
                    log.info("Proactive PROCESSING: Finished Proactive processing {}", new Date());
                } catch (Throwable t) {
                    log.error("Error while running Proactive Process" + t.getMessage());
                }

            }
        }
    }
}
