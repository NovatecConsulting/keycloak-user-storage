package com.example.keycloakuserstore.representations;

import com.example.keycloakuserstore.model.User;
import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserRepresentation extends AbstractUserAdapterFederatedStorage {
    public static final String PHONE_ATTR_KEY = "phone";
    User user;

    public UserRepresentation(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, User user) {
        super(session, realm, storageProviderModel);
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public void setUsername(String username) {
        // TODO
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals(PHONE_ATTR_KEY)) {
            return user.getPhone();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add(PHONE_ATTR_KEY, user.getPhone());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals(PHONE_ATTR_KEY)) {
            List<String> phone = new LinkedList<>();
            phone.add(user.getPhone());
            return phone;
        } else {
            return super.getAttribute(name);
        }
    }


    @Override
    public String getId() {
        return StorageId.keycloakId(storageProviderModel, user.getId().toString());
    }
}
