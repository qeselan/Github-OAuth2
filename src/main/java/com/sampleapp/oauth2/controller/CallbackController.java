package com.sampleapp.oauth2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampleapp.oauth2.domain.BearerTokenResponse;
import com.sampleapp.oauth2.domain.OAuth2Configuration;
import com.sampleapp.oauth2.domain.UserDetails;
import com.sampleapp.oauth2.helper.HttpHelper;
import com.sampleapp.oauth2.service.UserService;
import com.sampleapp.oauth2.session.AccessToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author otaniyici
 *
 */
@Controller
public class CallbackController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessToken accessToken;
    
    @Autowired
    public OAuth2Configuration oAuth2Configuration;
    
    @Autowired
    public HttpHelper httpHelper;
    
    private static ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(CallbackController.class);
    
    /**
     *  This is the redirect handler
     *  The Authorization code has a short lifetime.
     *  Hence Unless a user action is quick and mandatory, proceed to exchange the Authorization Code for
     *  BearerToken
     *      
     * @param authCode
     * @param state
     * @param session
     * @return
     */
    @RequestMapping("/oauth2redirect")
    public String callBackFromOAuth(@RequestParam("code") String authCode, @RequestParam("state") String state, HttpSession session) {
        logger.debug("inside oauth2redirect " + authCode  );
        
        String csrfToken = (String) session.getAttribute("csrfToken");
        if (csrfToken.equals(state)) {
            session.setAttribute("auth_code", authCode);
            BearerTokenResponse bearerTokenResponse = retrieveBearerTokens(authCode, session);  
            
            /*
             * save token to session
             * In real usecase, this is where tokens would have to be persisted (to a SQL DB, for example). 
             * Update your Datastore here with user's AccessToken
            */
            if (bearerTokenResponse != null) {
                accessToken.setPayload(bearerTokenResponse);
            }

            if (accessToken.hasUserEmailScope()) {
                UserDetails userDetails = userService.getUserDetails();
                session.setAttribute("givenName", userDetails.getLogin());
            }
            return "connected";
        }
        logger.info("csrf token mismatch " );
        return null;
    }

    private BearerTokenResponse retrieveBearerTokens(String auth_code, HttpSession session) {
        logger.info("inside bearer tokens");

        HttpPost post = new HttpPost(oAuth2Configuration.getAccessTokenEndpoint());

        List<NameValuePair> urlParameters = httpHelper.getUrlParameters(session);

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            String result = httpHelper.executeRequest(post);
            return mapper.readValue(result, BearerTokenResponse.class);
            
        } catch (Exception ex) {
            logger.error("Exception while retrieving bearer tokens", ex);
        }
        return null;
    }
}
