package com.sampleapp.oauth2.controller;

import com.sampleapp.oauth2.domain.OAuth2Configuration;
import com.sampleapp.oauth2.helper.HttpHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author otaniyici
 *
 */
@Controller
public class OAuth2Controller {
	
	private static final Logger logger = Logger.getLogger(OAuth2Controller.class);
	
	@Autowired
    public OAuth2Configuration oAuth2Configuration;
	
	@Autowired
    public HttpHelper httpHelper;
	    
	@RequestMapping("/")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/connected")
	public String connected() {
		return "connected";
	}
	
	/**
	 * Controller mapping for signInWithGithub button
	 * @return
	 */
	@RequestMapping("/signInWithGithub")
	public View signInWithGithub(HttpSession session) {
		logger.info("inside signInWithGithub ");
		return new RedirectView(prepareUrl(oAuth2Configuration.getScope(), generateCSRFToken(session)), false, true, false);
	}
	
	private String prepareUrl(String scope, String csrfToken)  {
		try {
			return oAuth2Configuration.getAuthorizationEndpoint()
					+ "?redirect_uri=" + URLEncoder.encode(oAuth2Configuration.getAppRedirectUri(), "UTF-8")
					+ "&client_id=" + oAuth2Configuration.getAppClientId()
					+ "&scope=" + URLEncoder.encode(scope, "UTF-8")
					+ "&state=" + csrfToken;

		} catch (UnsupportedEncodingException e) {
			logger.error("Exception while preparing url for redirect ", e);
		}
		return null;
	}
	
	private String generateCSRFToken(HttpSession session)  {
		String csrfToken = UUID.randomUUID().toString();
		session.setAttribute("csrfToken", csrfToken);
		return csrfToken;
	}

}
