package com.sampleapp.oauth2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampleapp.oauth2.domain.Email;
import com.sampleapp.oauth2.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private static ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(UserController.class);

    private static final String USER_EMAIL = "/user/emails";

    /**
     * Endpoint
     * @return
     */
    @GetMapping("/user/emails")
    public ResponseEntity<Email[]> getUserEmails(HttpSession session) {
        Email[] userEmails = userService.getUserEmails();
        return ResponseEntity.ok(userEmails);
    }
}
