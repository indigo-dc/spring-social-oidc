package org.springframework.social.oidc.deep.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oidc.deep.api.DeepOrchestrator;

import java.security.KeyStore;

public class OidcConnectionFactory extends OAuth2ConnectionFactory<DeepOrchestrator> {

  public OidcConnectionFactory(
      String orchestratorUrl,
      KeyStore orchestratorCert,
      String baseUrl,
      String clientId,
      String clientSecret) {
    super(
        "oidc",
        new OidcProvider(orchestratorUrl, orchestratorCert, baseUrl, clientId, clientSecret),
        new OidcAdapter());
  }
}
