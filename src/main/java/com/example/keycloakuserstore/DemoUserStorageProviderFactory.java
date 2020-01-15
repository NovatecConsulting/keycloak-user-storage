package com.example.keycloakuserstore;

import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import lombok.extern.jbosslog.JBossLog;

/**
 * DemoUserStorageProviderFactory
 */
@JBossLog
public class DemoUserStorageProviderFactory implements UserStorageProviderFactory<DemoUserStorageProvider> {

    @Override
    public void init(Config.Scope config) {
  
      // this configuration is pulled from the SPI configuration of this provider in the standalone[-ha] / domain.xml
      // see setup.cli
  
      String someProperty = config.get("someProperty");
      log.infov("Configured {0} with someProperty: {1}", this, someProperty);
    }

    @Override
    public DemoUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        log.infov("Create new provider {0}", "demo-storage");
        UserRepository repository = new UserRepository();
        return new DemoUserStorageProvider(session, model, repository);
    }

    @Override
    public String getId() {
        return "demo-user-provider";
    }
}