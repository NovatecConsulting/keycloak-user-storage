package com.example.keycloakuserstore;

import com.example.keycloakuserstore.services.UserService;
import com.example.keycloakuserstore.services.impl.UserServiceImpl;
import com.example.keycloakuserstore.utils.OAuthClientCredentialsAuthenticator;
import lombok.extern.jbosslog.JBossLog;
import okhttp3.OkHttpClient;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

/**
 * DemoUserStorageProviderFactory
 */
@JBossLog
public class DemoUserStorageProviderFactory implements UserStorageProviderFactory<DemoUserStorageProvider> {

    @Override
    public DemoUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        log.infov("Create new provider {0}", "demo-storage");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .authenticator(new OAuthClientCredentialsAuthenticator("http://127.0.0.1:30000/auth/realms/Testrealm/protocol/openid-connect/token", "federation-client", "44f3b18e-1939-4391-a2f9-479621888a61"))
                .build();
        UserService userService = new UserServiceImpl(httpClient, "http://127.0.0.1:31000/user");
        return new DemoUserStorageProvider(session, model, userService);
    }

    @Override
    public String getId() {
        return "demo-user-provider";
    }
}