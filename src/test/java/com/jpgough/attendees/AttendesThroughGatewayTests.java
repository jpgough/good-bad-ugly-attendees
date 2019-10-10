package com.jpgough.attendees;


import com.github.dockerjava.api.model.Volumes;
import com.github.dockerjava.api.model.VolumesFrom;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class AttendesThroughGatewayTests {

    private static final int EXPOSED_GATEWAY_PORT = 8080;
//    private static final VolumesFroms VOLUMES_FROMS = new VolumesFrom();

    @Rule
    public GenericContainer redis = new GenericContainer<>("jpgough/vanilla-scg")
            .withExposedPorts(EXPOSED_GATEWAY_PORT)
            .withClasspathResourceMapping("gateway/application.yaml",
                    "/etc/gateway/config/application.yaml",
                    BindMode.READ_ONLY);

    @Test
    public void contextLoads() {
    }

    @Test
    public void hit_service(){
        String address = redis.getContainerIpAddress();
        Integer port = redis.getFirstMappedPort();

        String  endpoint = "http://"+address + ":" + port + "/num/42";
        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<String> forEntity = restTemplate.getForEntity(endpoint, String.class);
        System.out.println(forEntity);
    }


}
