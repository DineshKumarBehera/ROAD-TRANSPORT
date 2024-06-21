package com.rbc.zfe0.road.eod.scheduler;

import com.rbc.zfe0.road.eod.process.handler.EodHandler;
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
public class PhysicalTransferEodCronJob {

    @Value("${rbc.road.scheduler.eod.run-eod-scheduler}")
    private Boolean runEodScheduler;

    @Autowired
    EodHandler eodHandler;

    @Autowired
    HolidayUtil holidayUtil;

    @Scheduled(cron = "${rbc.road.scheduler.eod.cron-expression:}",
            zone = "${rbc.road.scheduler.eod.time-zone:}")
    @Transactional
    public void runEndOfDayJobs() {

        //Run only on Week days excluding RBC public holidays
        if (!holidayUtil.isHoliday()) {
            if (runEodScheduler) {
                try {
                    log.info("EOD PROCESSING: Starting End of Day (EOD) processing {}", new Date());
                    eodHandler.runEod();
                    log.info("EOD PROCESSING: Finished End of Day (EOD) processing {}", new Date());
                } catch (Throwable t) {
                    log.error("Error while running EOD Process" + t.getMessage());
                }

            }
        }
    }
}
