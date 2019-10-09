package com.jpgough.userdetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserDetailsController {

    private Logger log = LoggerFactory.getLogger(UserDetailsController.class);

    @RequestMapping(value = "/user/{userId}", method= RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public UserDetail delay(@PathVariable Integer userId, @RequestParam( value = "delay", required = false) Integer delay) {
        if(delay == null) {
            delay = 0;
        }

        try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UserDetail user = new UserDetail();
        user.setUserId(userId);
        user.setEmail("jpgough@gmail.com");
        user.setName("James Gough");
        return user;
    }

    @DeleteMapping("user/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttendee(@PathVariable Integer id, @RequestBody UserDetail body) {
        log.info(body.toString());
    }
}
