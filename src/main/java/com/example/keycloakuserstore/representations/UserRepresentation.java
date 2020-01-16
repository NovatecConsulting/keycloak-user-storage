package com.example.keycloakuserstore.representations;

import com.example.keycloakuserstore.models.User;
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
    User userEntity;

    public UserRepresentation(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, User userEntity) {
        super(session, realm, storageProviderModel);
        this.userEntity = userEntity;
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public void setSingleAttribute(String name, String value) {
        if (name.equals("phone")) {
            userEntity.setPhone(value);
        } else {
            super.setSingleAttribute(name, value);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (name.equals("phone")) {
            userEntity.setPhone(null);
        } else {
            super.removeAttribute(name);
        }
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        if (name.equals("phone")) {
            userEntity.setPhone(values.get(0));
        } else {
            super.setAttribute(name, values);
        }
    }

    @Override
    public String getFirstAttribute(String name) {
        if (name.equals("phone")) {
            return userEntity.getPhone();
        } else {
            return super.getFirstAttribute(name);
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> attrs = super.getAttributes();
        MultivaluedHashMap<String, String> all = new MultivaluedHashMap<>();
        all.putAll(attrs);
        all.add("phone", userEntity.getPhone());
        return all;
    }

    @Override
    public List<String> getAttribute(String name) {
        if (name.equals("phone")) {
            List<String> phone = new LinkedList<>();
            phone.add(userEntity.getPhone());
            return phone;
        } else {
            return super.getAttribute(name);
        }
    }

    @Override
    public String getId() {
        return StorageId.keycloakId(storageProviderModel, userEntity.getId().toString());
    }
}
