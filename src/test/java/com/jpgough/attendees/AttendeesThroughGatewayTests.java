package com.jpgough.attendees;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=12345"}
)
@DirtiesContext
@Testcontainers
public class AttendeesThroughGatewayTests {

    private static final int EXPOSED_GATEWAY_PORT = 8080;

    @Container
//    @Rule
    public GenericContainer scgVanillaContainer = new GenericContainer<>("jpgough/vanilla-scg")
            .withExposedPorts(EXPOSED_GATEWAY_PORT)
            .withEnv("HOST_MACHINE_IP_ADDRESS", IPUtils.myIPAddress())
            .withClasspathResourceMapping("gateway/application.yaml",
                    "/etc/gateway/config/application.yaml",
                    BindMode.READ_ONLY);

    @Test
    public void delete_call_with_body_supported_by_spring_cloud_gateway() {
        String address = scgVanillaContainer.getContainerIpAddress();
        Integer port = scgVanillaContainer.getFirstMappedPort();

        String endpoint = "http://" + address + ":" + port + "/attendees/user/5";
        RestTemplate restTemplate = new RestTemplate();
        UserDetail user = new UserDetail();
        user.setUserId(5);
        user.setName("Test User");
        user.setEmail("test@email.com");

        Map<String, String> uriVariables = new HashMap<>();
        HttpEntity<UserDetail> requestEntity = new HttpEntity<>(user);
        ResponseEntity<UserDetail> responseEntity = restTemplate.exchange(endpoint, HttpMethod.DELETE,
                requestEntity, UserDetail.class, uriVariables);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void get_single_attendee_from_service() {
        String address = scgVanillaContainer.getContainerIpAddress();
        Integer port = scgVanillaContainer.getFirstMappedPort();

        String endpoint = "http://" + address + ":" + port + "/attendees/cached?number=1";
        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
