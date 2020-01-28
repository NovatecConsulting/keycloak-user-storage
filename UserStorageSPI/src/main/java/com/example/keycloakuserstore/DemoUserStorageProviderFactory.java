package com.example.keycloakuserstore;

import com.example.keycloakuserstore.services.UserService;
import com.example.keycloakuserstore.services.impl.UserServiceImpl;
import com.example.keycloakuserstore.oauth.OAuthClientCredentialsAuthenticator;
import lombok.extern.jbosslog.JBossLog;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

/**
 * DemoUserStorageProviderFactory
 */
@JBossLog
public class DemoUserStorageProviderFactory implements UserStorageProviderFactory<DemoUserStorageProvider> {

    protected static final List<ProviderConfigProperty> configMetadata;

    public static final String API_URL_KEY = "api:url";
    public static final String OAUTH_TOKEN_URL = "api:oauth:tokenUrl";
    public static final String OAUTH_CLIENT_ID = "api:oauth:clientId";
    public static final String OAUTH_CLIENT_SECRET = "api:oauth:clientSecret";


    static {
        configMetadata = ProviderConfigurationBuilder.create()
                .property().name(API_URL_KEY)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("API URL")
                .defaultValue("http://localhost:31000/user")
                .helpText("URL of the REST APIs User Endpoint.")
                .add()

                .property().name(OAUTH_TOKEN_URL)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("OAuth Token Endpoint")
                .defaultValue("http://127.0.0.1:30000/auth/realms/Testrealm/protocol/openid-connect/token")
                .helpText("URL of the token endpoint to fetch an access token using the client credentials grant. The token is used for authorization to the user API.")
                .add()

                .property().name(OAUTH_CLIENT_ID)
                .type(ProviderConfigProperty.STRING_TYPE)
                .label("OAuth Client ID")
                .defaultValue("federation-client")
                .helpText("Client ID for authentication to the token endpoint.")
                .add()

                .property().name(OAUTH_CLIENT_SECRET)
                .type(ProviderConfigProperty.PASSWORD)
                .label("OAuth Client Secret")
                .defaultValue("44f3b18e-1939-4391-a2f9-479621888a61")
                .helpText("Client Secret for authentication to the token endpoint.")
                .add()
                .build();
    }

    @Override
    public DemoUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        log.infov("Create new provider {0}", "demo-storage");
        MultivaluedHashMap<String, String> config = model.getConfig();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .authenticator(new OAuthClientCredentialsAuthenticator(config.getFirst(OAUTH_TOKEN_URL), config.getFirst(OAUTH_CLIENT_ID), config.getFirst(OAUTH_CLIENT_SECRET)))
                .build();
        UserService userService = new UserServiceImpl(httpClient, config.getFirst(API_URL_KEY));
        return new DemoUserStorageProvider(session, model, userService);
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) throws ComponentValidationException {
        MultivaluedHashMap<String, String> configMap = config.getConfig();
        if(StringUtils.isBlank(configMap.getFirst(API_URL_KEY))) {
            throw new ComponentValidationException("API URL empty.");
        }
        if(StringUtils.isBlank(configMap.getFirst(OAUTH_TOKEN_URL))) {
            throw new ComponentValidationException("OAuth token URL empty.");
        }
        if(StringUtils.isBlank(configMap.getFirst(OAUTH_CLIENT_ID))){
            throw new ComponentValidationException("OAuth Client ID empty.");
        }
        if(StringUtils.isBlank(configMap.getFirst(OAUTH_CLIENT_SECRET))) {
            throw new ComponentValidationException("OAuth Client Secret empty.");
        }
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public String getId() {
        return "demo-user-provider";
    }
}