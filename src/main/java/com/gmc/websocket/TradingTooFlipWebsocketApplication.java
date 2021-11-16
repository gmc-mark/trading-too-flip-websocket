package com.gmc.websocket;

import com.gmc.websocket.schedualer.JobSetting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.websocket.DeploymentException;
import java.io.IOException;

@SpringBootApplication
public class TradingTooFlipWebsocketApplication {
    private final JobSetting jobSetting;

    public TradingTooFlipWebsocketApplication(JobSetting jobSetting) {
        this.jobSetting = jobSetting;
    }

    public static void main(String[] args) {
        SpringApplication.run(TradingTooFlipWebsocketApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    void applicaitonReady() throws DeploymentException, IOException {
        jobSetting.start();
    }
}
