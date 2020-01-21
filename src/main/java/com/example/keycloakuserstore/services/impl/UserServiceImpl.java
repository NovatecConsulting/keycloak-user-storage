package com.example.keycloakuserstore.services.impl;

import com.example.keycloakuserstore.errors.UserStorageException;
import com.example.keycloakuserstore.errors.UserStorageUnavailableException;
import com.example.keycloakuserstore.model.User;
import com.example.keycloakuserstore.services.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    OkHttpClient httpClient;
    String baseUrl;
    private static Gson gson = new Gson();

    public UserServiceImpl(OkHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<User> getUserById(UUID id) throws UserStorageException {
        Request request = new Request.Builder()
                .url(baseUrl + "/user/" + id.toString())
                .build();

        return executeUserRequest(request);
    }

    @Override
    public List<User> getAllUsers() throws UserStorageException {
        Request request = new Request.Builder()
                .url(baseUrl + "/user/")
                .build();

        return executeUserListRequest(request);
    }

    @Override
    public Optional<User> updateUser(User updatedUser) throws UserStorageException {
        return null;
    }

    @Override
    public Optional<User> createUser(User user) throws UserStorageException {
        return null;
    }

    @Override
    public List<User> findUserByUsernameOrEmail(String username, String email) throws UserStorageException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/user/").newBuilder();
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
