package com.example.keycloakuserstore.oauth;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

public class OAuthClientCredentialsAuthenticator implements Authenticator {

    private String tokenEndpointURI;
    private String clientId;
    private String clientSecret;
    private final OkHttpClient client = new OkHttpClient();

    private String accessToken;
    private long validUntil;

    public OAuthClientCredentialsAuthenticator(String tokenEndpointURI, String clientId, String clientSecret) {
        this.tokenEndpointURI = tokenEndpointURI;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }


    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        if(response.request().header("Authorization") != null) {
            if(validUntil > System.currentTimeMillis())
            return null; // authentication failed
        }

        RequestBody form = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .build();

        Request request = new Request.Builder()
                .url(tokenEndpointURI)
                .post(form)
                .build();

        try (Response authResponse = client.newCall(request).execute()) {
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(authResponse.body().string(), Map.class);
            accessToken = (String)data.get("access_token");
            if(accessToken != null) {
                validUntil = System.currentTimeMillis() + ((Double)data.get("expires_in")).longValue();
                return response.request().newBuilder()
                        .header("Authorization", "Bearer " + accessToken)
                        .build();
            }
        }
        return null;
    }
}
