package com.jpgough.attendees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AttendeesController {

    private Logger log = LoggerFactory.getLogger(AttendeesController.class);

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/attendees")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Attendee> attendeeList(@RequestParam(value = "number", required = true) Integer number,
            @RequestParam(value = "delay", required = false) Integer delay) {
        List<Attendee> attendees = new ArrayList<>();

        for(int i=0; i < number; i++) {
            Map<String, String> requestVariables = new HashMap<>();
            if(delay == null) {
                delay = 0;
            }

            UserDetail userDetail = restTemplate.getForObject("http://gateway:8082/users/user/5?delay={delay}", UserDetail.class, delay);
            Attendee attendee = new Attendee();
            attendee.setUserDetail(userDetail);
            attendees.add(attendee);
        }

        return attendees;
    }

    @GetMapping(value="/cached")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Attendee> cachedList(@RequestParam(value = "delay", required = false) Integer delay) {
        if(delay == null) {
            delay = 0;
        }

        try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Attendee> attendees = new ArrayList<>();
        Attendee attendee = new Attendee();
        UserDetail user = new UserDetail();
        user.setEmail("test@user.com");
        user.setName("Test User");
        user.setUserId(5);
        attendee.setUserDetail(user);
        attendees.add(attendee);

        return attendees;
    }

    @DeleteMapping("user/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttendee(@PathVariable Integer id, @RequestBody UserDetail body) {
        log.info(body.toString());
    }
}
