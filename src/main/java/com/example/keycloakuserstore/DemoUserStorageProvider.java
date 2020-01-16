package com.example.keycloakuserstore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.keycloakuserstore.dao.UserDAO;
import com.example.keycloakuserstore.models.User;
import com.example.keycloakuserstore.representations.UserRepresentation;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import javax.swing.*;

public class DemoUserStorageProvider implements UserStorageProvider,
		UserLookupProvider,
		UserRegistrationProvider,
		UserQueryProvider,
		CredentialInputUpdater,
		CredentialInputValidator {

	private final KeycloakSession session;
	private final ComponentModel model;
	private final UserDAO userDAO;
		  
	public DemoUserStorageProvider(KeycloakSession session, ComponentModel model, UserDAO userDAO) {
	  this.session = session;
	  this.model = model;
	  this.userDAO = userDAO;
	}

	@Override
	public void close() {
		userDAO.close();
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
		if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
		User userModel = new User().setUsername(user.getUsername());
		userModel.setPassword(input.getChallengeResponse());
		userDAO.createUser(userModel);

		return true;
	}

	@Override
	public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUsersCount(RealmModel realm) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm) {
		// TODO
		return new ArrayList<>();
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult,
			int maxResults) {
		return userDAO.findAll(firstResult, maxResults)
				.stream()
				.map(user -> new UserRepresentation(session, realm, model, user))
				.collect(Collectors.toList());
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public UserModel getUserById(String id, RealmModel realm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel getUserByUsername(String username, RealmModel realm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel addUser(RealmModel realm, String username) {
		User user = new User();
		user.setUsername(username);
		user = userDAO.createUser(user);

		User finalUser = user;
		return new UserModel() {
			@Override
			public String getId() {
				return finalUser.getId()+"";
			}

			@Override
			public String getUsername() {
				return finalUser.getUsername();
			}

			@Override
			public void setUsername(String username) {

			}

			@Override
			public Long getCreatedTimestamp() {
				return null;
			}

			@Override
			public void setCreatedTimestamp(Long timestamp) {

			}

			@Override
			public boolean isEnabled() {
				return false;
			}

			@Override
			public void setEnabled(boolean enabled) {

			}

			@Override
			public void setSingleAttribute(String name, String value) {

			}

			@Override
			public void setAttribute(String name, List<String> values) {

			}

			@Override
			public void removeAttribute(String name) {

			}

			@Override
			public String getFirstAttribute(String name) {
				return null;
			}

			@Override
			public List<String> getAttribute(String name) {
				return null;
			}

			@Override
			public Map<String, List<String>> getAttributes() {
				return null;
			}

			@Override
			public Set<String> getRequiredActions() {
				return null;
			}

			@Override
			public void addRequiredAction(String action) {

			}

			@Override
			public void removeRequiredAction(String action) {

			}

			@Override
			public void addRequiredAction(RequiredAction action) {

			}

			@Override
			public void removeRequiredAction(RequiredAction action) {

			}

			@Override
			public String getFirstName() {
				return null;
			}

			@Override
			public void setFirstName(String firstName) {

			}

			@Override
			public String getLastName() {
				return null;
			}

			@Override
			public void setLastName(String lastName) {

			}

			@Override
			public String getEmail() {
				return null;
			}

			@Override
			public void setEmail(String email) {

			}

			@Override
			public boolean isEmailVerified() {
				return false;
			}

			@Override
			public void setEmailVerified(boolean verified) {

			}

			@Override
			public Set<GroupModel> getGroups() {
				return null;
			}

			@Override
			public void joinGroup(GroupModel group) {

			}

			@Override
			public void leaveGroup(GroupModel group) {

			}

			@Override
			public boolean isMemberOf(GroupModel group) {
				return false;
			}

			@Override
			public String getFederationLink() {
				return null;
			}

			@Override
			public void setFederationLink(String link) {

			}

			@Override
			public String getServiceAccountClientLink() {
				return null;
			}

			@Override
			public void setServiceAccountClientLink(String clientInternalId) {

			}

			@Override
			public Set<RoleModel> getRealmRoleMappings() {
				return null;
			}

			@Override
			public Set<RoleModel> getClientRoleMappings(ClientModel app) {
				return null;
			}

			@Override
			public boolean hasRole(RoleModel role) {
				return false;
			}

			@Override
			public void grantRole(RoleModel role) {

			}

			@Override
			public Set<RoleModel> getRoleMappings() {
				return null;
			}

			@Override
			public void deleteRoleMapping(RoleModel role) {

			}
		};
	}

	@Override
	public boolean removeUser(RealmModel realm, UserModel user) {
		return false;
	}
}
