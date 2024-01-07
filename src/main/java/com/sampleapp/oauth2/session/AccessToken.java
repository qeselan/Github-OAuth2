package com.sampleapp.oauth2.session;

import com.sampleapp.oauth2.domain.BearerTokenResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccessToken {
    private BearerTokenResponse payload;

    public BearerTokenResponse getPayload() {
        return payload;
    }

    public void setPayload(BearerTokenResponse payload) {
        this.payload = payload;
    }

    public boolean hasUserEmailScope() {
        String scope = this.payload.getScope();
        return scope.contains("user:email") || scope.contains("user");
    }
}
