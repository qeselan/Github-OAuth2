package com.sampleapp.oauth2.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampleapp.oauth2.domain.OAuth2Configuration;
import com.sampleapp.oauth2.session.AccessToken;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author otaniyici
 *
 */
@Service
public class HttpHelper {

	@Autowired
	private AccessToken accessToken;
	
	@Autowired
    private OAuth2Configuration oAuth2Configuration;

	@Autowired
	private HttpClient httpClient;

	private static ObjectMapper mapper = new ObjectMapper();
	private static final Logger logger = Logger.getLogger(HttpHelper.class);
	
	public HttpUriRequest addHeader(HttpUriRequest request) {
		request.setHeader("Accept", "application/json");
        return request;
	}

	public List<NameValuePair> getUrlParameters(HttpSession session) {
		List<NameValuePair> urlParameters = new ArrayList<>();
		String auth_code = (String)session.getAttribute("auth_code");
		urlParameters.add(new BasicNameValuePair("code", auth_code));
		urlParameters.add(new BasicNameValuePair("redirect_uri", oAuth2Configuration.getAppRedirectUri()));
		urlParameters.add(new BasicNameValuePair("client_secret", oAuth2Configuration.getAppClientSecret()));
		urlParameters.add(new BasicNameValuePair("client_id", oAuth2Configuration.getAppClientId()));
		return urlParameters;
	}

	public HttpGet addAuthorizationHeader(HttpGet request) {
		request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getPayload().getAccessToken());
		return request;
	}
	
	public StringBuffer getResult(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
		    result.append(line);
		}
		return result;
	}

	public String executeRequest(HttpUriRequest request) {
		try {
			request = this.addHeader(request);
			HttpResponse response = httpClient.execute(request);
			logger.info("Response Code : "+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.info("failed getting access token");
				return null;
			}

			StringBuffer result = this.getResult(response);
			logger.debug("raw result= " + result);
			return result.toString();
		} catch (Exception ex) {
			logger.error("Exception while executing query", ex);
		}

		return null;
	}

	public <T> T readValue(HttpUriRequest request, Class<T> valueType) {
		T result = null;
		try {
			result = (T) mapper.readValue(this.executeRequest(request), valueType);
		} catch (IOException e) {
			logger.error("Exception while deserializing");
		}

		return result;
	}

}
