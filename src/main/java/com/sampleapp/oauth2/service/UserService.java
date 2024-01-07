package com.sampleapp.oauth2.service;
import com.sampleapp.oauth2.domain.Email;
import com.sampleapp.oauth2.domain.OAuth2Configuration;
import com.sampleapp.oauth2.domain.UserDetails;
import com.sampleapp.oauth2.helper.HttpHelper;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private OAuth2Configuration oAuth2Configuration;

    @Autowired
    private HttpHelper httpHelper;


    private static final String USER = "/user";
    private static final String USER_EMAIL = "/user/emails";

    public UserDetails getUserDetails() {
        HttpGet get = new HttpGet(oAuth2Configuration.getResourceServerURL() + USER);
        get = httpHelper.addAuthorizationHeader(get);
        return httpHelper.readValue(get, UserDetails.class);
    }

    public Email[] getUserEmails() {
        HttpGet get = new HttpGet(oAuth2Configuration.getResourceServerURL() + USER_EMAIL);
        get = httpHelper.addAuthorizationHeader(get);
        return httpHelper.readValue(get, Email[].class);
    }

}
