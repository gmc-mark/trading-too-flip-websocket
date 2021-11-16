package com.gmc.websocket.schedualer;

import com.gmc.websocket.okex.OkexWebSocketClientEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@Slf4j
@Component
public class WebsocketScheduler extends QuartzJobBean {

    final SchedulerFactoryBean schedulerFactoryBean;
    Session session;
    JobKey jobKey = null;

    public WebsocketScheduler(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        try {
            String uri = "wss://wsaws.okex.com:8443/ws/v5/public";
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxBinaryMessageBufferSize(1048576);
            container.setDefaultMaxTextMessageBufferSize(1048576);
            this.session = container.connectToServer(OkexWebSocketClientEndpoint.class, URI.create(uri));

            jobKey = jobExecutionContext.getJobDetail().getKey();
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            if (session.isOpen()) {
                scheduler.pauseJob(jobKey);
                log.info("스케줄러 작업 종료");
            }

        } catch (Exception e) {
            log.info("okexWebSocket exceptions");

            throw new RuntimeException(e);

        }
    }

}
