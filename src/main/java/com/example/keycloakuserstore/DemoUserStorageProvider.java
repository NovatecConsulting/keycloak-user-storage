package com.example.keycloakuserstore;

import com.example.keycloakuserstore.errors.UserStorageException;
import com.example.keycloakuserstore.model.User;
import com.example.keycloakuserstore.representations.UserRepresentation;
import com.example.keycloakuserstore.services.UserService;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@JBossLog
public class DemoUserStorageProvider implements
		UserStorageProvider,
		UserLookupProvider,
		UserQueryProvider,
		UserRegistrationProvider {

	private final KeycloakSession session;
	private final ComponentModel model;
	private final UserService userService;

	public DemoUserStorageProvider(KeycloakSession session, ComponentModel model, UserService userService) {
	  this.session = session;
	  this.model = model;
	  this.userService = userService;
	}

	@Override
	public void close() {
		log.infov("public void close()");
	}

	@Override
	public int getUsersCount(RealmModel realm) {
		log.infov("public int getUsersCount(RealmModel realm)");
		return getUsers(realm).size();
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm) {
		log.infov("public List<UserModel> getUsers(RealmModel realm)");
		try {
			return userService.getAllUsers()
					.stream()
					.map(user -> new UserRepresentation(session, realm, model, user))
					.collect(Collectors.toList());
		} catch (UserStorageException exception) {
			return null;
		}
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
		log.infov("public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults)");
		// TODO: Pagination
		return getUsers(realm);
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm) {
		log.infov("public List<UserModel> searchForUser(String search, RealmModel realm)");
		try {
			return userService.findUserByUsernameOrEmail(search, search)
					.stream()
					.map(user -> new UserRepresentation(session, realm, model, user))
					.collect(Collectors.toList());
		} catch (UserStorageException exception) {
			return null;
		}
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
		log.infov("public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults)");
		// TODO: Pagination
		return searchForUser(search, realm);
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
		log.infov("public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm)");
		try {
			return userService.getAllUsers()
					.stream()
					.map(user -> new UserRepresentation(session, realm, model, user))
					.collect(Collectors.toList());
		} catch (UserStorageException exception) {
			return null;
		}
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult,
			int maxResults) {
		log.infov("public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults)");
		return searchForUser(params, realm);
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
		log.infov("public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults)");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
		log.infov("public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group)");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
		log.infov("public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm)");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel getUserById(String id, RealmModel realm) {
		log.infov("public UserModel getUserById(String id, RealmModel realm)");
		try {
			String externalId = StorageId.externalId(id);
			User user = userService.getUserById(UUID.fromString(externalId)).orElse(null);
			if(user == null) {
				return null;
			}
			return new UserRepresentation(session, realm, model, user);
		} catch (UserStorageException exception) {
			return null;
		}
	}

	@Override
	public UserModel getUserByUsername(String username, RealmModel realm) {
		log.infov("public UserModel getUserByUsername(String username, RealmModel realm)");
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
		log.infov("public UserModel getUserByEmail(String email, RealmModel realm)");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel addUser(RealmModel realm, String username) {
		log.infov("public UserModel addUser(RealmModel realm, String username)");
		try {
			Optional<User> userOptional = userService.createUser(new User().setUsername(username));
			if(!userOptional.isPresent()) {
				return null;
			}
			return new UserRepresentation(session, realm, model, userOptional.get());
		} catch (UserStorageException userStoragException) {
			return null;
		}
	}

	@Override
	public boolean removeUser(RealmModel realm, UserModel user) {
		log.infov("public boolean removeUser(RealmModel realm, UserModel user)");
		try {
			userService.deleteUser(user.getId());
			return true;
		} catch (UserStorageException e) {
			return false;
		}
	}
}
