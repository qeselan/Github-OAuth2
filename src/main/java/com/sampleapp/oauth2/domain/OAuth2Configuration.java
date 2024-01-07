package com.sampleapp.oauth2.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author otaniyici
 *
 */
@Configuration
@PropertySource(value="classpath:/application.properties", ignoreResourceNotFound=true)
public class OAuth2Configuration {
	
	@Autowired
    Environment env;

    public String getAppClientId() {
    	return env.getProperty("OAuth2AppClientId");
    }

    public String getAppClientSecret() {
        return env.getProperty("OAuth2AppClientSecret");
    }
    
    public String getAppRedirectUri() {
        return env.getProperty("OAuth2AppRedirectUri");
    }

	public String getScope() {
		return env.getProperty("OAuth2Scope");
	}

	public String getAccessTokenEndpoint() {
		return env.getProperty("OAuth2AccessTokenEndpoint");
	}

	public String getAuthorizationEndpoint() {
		return env.getProperty("OAuth2AuthorizationEndpoint");
	}

	public String getResourceServerURL() {
		return env.getProperty("OAuth2ResourceServerURL");
	}
}
