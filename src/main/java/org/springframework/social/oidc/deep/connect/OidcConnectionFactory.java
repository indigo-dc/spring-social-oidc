package org.springframework.social.oidc.deep.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oidc.deep.api.DeepOrchestrator;

import java.security.KeyStore;

public class OidcConnectionFactory extends OAuth2ConnectionFactory<DeepOrchestrator> {

  /**
   * Main constructor used by Spring applications.
   *
   * @param orchestratorUrl An URL pointing to the DEEP Orchestrator.
   * @param orchestratorCert A JKS keystore containing the orchestrator certificate in case it's
   *     self-signed or not valid. If the orchestrator is using a valid certificate, this parameter
   *     can be null.
   * @param baseUrl The IAM base URL to use.
   * @param clientId The Client Identifier to use as authentication.
   * @param clientSecret The client secret of the above client identifier.
   */
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
