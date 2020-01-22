package com.example.keycloakuserstore.services.impl;

import com.example.keycloakuserstore.errors.UserNotFoundException;
import com.example.keycloakuserstore.errors.UserStorageException;
import com.example.keycloakuserstore.errors.UserStorageUnavailableException;
import com.example.keycloakuserstore.model.User;
import com.example.keycloakuserstore.services.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final String MEDIA_TYPE_JSON = "application/json";
    OkHttpClient httpClient;
    String baseUrl;
    private static Gson gson = new Gson();

    public UserServiceImpl(OkHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = prepareBaseUrl(baseUrl);
    }

    private String prepareBaseUrl(String baseUrl) {
        if(!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }

    @Override
    public Optional<User> getUserById(UUID id) throws UserStorageException {
        Request request = new Request.Builder()
                .url(baseUrl + id.toString())
                .build();

        return executeUserRequest(request);
    }

    @Override
    public List<User> getAllUsers() throws UserStorageException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        return executeUserListRequest(request);
    }

    @Override
    public Optional<User> updateUser(User updatedUser) throws UserStorageException {
        return null;
    }

    @Override
    public Optional<User> createUser(User user) throws UserStorageException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(RequestBody.create(gson.toJson(user), MediaType.parse(MEDIA_TYPE_JSON)))
                .build();
        return executeUserRequest(request);
    }

    @Override
    public List<User> findUserByUsernameOrEmail(String username, String email) throws UserStorageException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        if(username != null && !username.isEmpty()) {
            urlBuilder.addQueryParameter("username", username);
        }
        if(email != null && !email.isEmpty()) {
            urlBuilder.addQueryParameter("email", email);
        }
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        return executeUserListRequest(request);
    }

    @Override
    public List<User> findUserByUsername(String username) throws UserStorageException {
        return null;
    }

    @Override
    public List<User> findUserByEmail(String email) throws UserStorageException {
        return null;
    }

    @Override
    public void deleteUser(String id) throws UserStorageException {
        Request request = new Request.Builder()
                .url(baseUrl + id)
                .delete()
                .build();

        try(Response response = httpClient.newCall(request).execute()) {
            if(response.code() == HTTP_STATUS_NOT_FOUND) {
                throw new UserNotFoundException();
            }
            if(!response.isSuccessful()) {
                throw new UserStorageException();
            }
        } catch (IOException ioex) {
            throw new UserStorageException();
        }
    }

    private List<User> executeUserListRequest(Request request) throws UserStorageException {
        try(Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            if(response.body() == null || responseBody.isEmpty()) {
                throw new UserStorageException();
            }
            Type userListType = new TypeToken<List<User>>(){}.getType();
            List<User> users = gson.fromJson(responseBody, userListType);

            return users;
        } catch (IOException e) {
            throw new UserStorageUnavailableException();
        }
    }

    private Optional<User> executeUserRequest(Request request) throws UserStorageException {
        try(Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            if(response.body() == null || responseBody.isEmpty()) {
                throw new UserStorageException();
            }
            User user = gson.fromJson(responseBody, User.class);

            return Optional.ofNullable(user);
        } catch (IOException e) {
            throw new UserStorageUnavailableException();
        }
    }


}
