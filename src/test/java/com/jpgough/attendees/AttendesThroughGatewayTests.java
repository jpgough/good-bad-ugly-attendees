package com.jpgough.attendees;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=12345"}
)
@DirtiesContext
public class AttendesThroughGatewayTests {

    private static final int EXPOSED_GATEWAY_PORT = 8080;

    @Rule
    public GenericContainer scgVanillaContainer = new GenericContainer<>("jpgough/vanilla-scg")

            .withExposedPorts(EXPOSED_GATEWAY_PORT)
            .withEnv("HOST_MACHINE_IP_ADDRESS", IPUtils.myIPAddress())
            .withClasspathResourceMapping("gateway/application.yaml",
                    "/etc/gateway/config/application.yaml",
                    BindMode.READ_ONLY);


    @Test
    public void contextLoads() {
    }

    @Test
    public void hit_service() {
        String address = scgVanillaContainer.getContainerIpAddress();
        Integer port = scgVanillaContainer.getFirstMappedPort();

        String endpoint = "http://" + address + ":" + port + "/a/cached?number=1";
        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
