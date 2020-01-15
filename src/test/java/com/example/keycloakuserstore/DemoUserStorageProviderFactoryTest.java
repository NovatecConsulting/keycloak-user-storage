package com.example.keycloakuserstore;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;

class DemoUserStorageProviderFactoryTest {

	@Test
	void createProvider() {
		DemoUserStorageProviderFactory factory = new DemoUserStorageProviderFactory();
		DemoUserStorageProvider provider = factory.create(mock(KeycloakSession.class), mock(ComponentModel.class));
		assertThat(provider).isNotNull();
	}

}
