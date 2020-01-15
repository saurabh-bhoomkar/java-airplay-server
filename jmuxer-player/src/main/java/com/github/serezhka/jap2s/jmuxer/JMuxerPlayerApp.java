package com.github.serezhka.jap2s.jmuxer;

import com.github.serezhka.jap2lib.AirPlayBonjour;
import com.github.serezhka.jap2s.receiver.AirTunesServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@SpringBootApplication
public class JMuxerPlayerApp {

    private final AirTunesServer airTunesServer;
    private final AirPlayBonjour airPlayBonjour;

    @Value("${airplay.port}")
    private int airPlayPort;

    @Value("${airtunes.port}")
    private int airTunesPort;

    @Autowired
    public JMuxerPlayerApp(AirTunesServer airTunesServer,
                           AirPlayBonjour airPlayBonjour) {
        this.airTunesServer = airTunesServer;
        this.airPlayBonjour = airPlayBonjour;
    }

    public static void main(String[] args) {
        SpringApplication.run(JMuxerPlayerApp.class, args);
    }

    @PostConstruct
    private void postConstruct() throws Exception {
        airPlayBonjour.start(airPlayPort, airTunesPort);
        new Thread(airTunesServer).start();
        log.info("AirTunes server started!");
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        airPlayBonjour.stop();
        log.info("AirTunes server stopped!");
    }
}