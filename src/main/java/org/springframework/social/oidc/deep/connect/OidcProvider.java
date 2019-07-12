package org.springframework.social.oidc.deep.connect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oidc.deep.api.DeepOrchestrator;
import org.springframework.social.oidc.deep.api.OidcConfiguration;
import org.springframework.social.oidc.deep.api.impl.DeepOrchestratorTemplate;
import org.springframework.web.client.RestTemplate;

import java.security.KeyStore;

public class OidcProvider extends AbstractOAuth2ServiceProvider<DeepOrchestrator> {

  private static final Log logger = LogFactory.getLog(OidcProvider.class);

  private OidcConfiguration configuration;
  private String orchestratorUrl;
  private KeyStore orchestratorCert;

  /**
   * Creates a OIDC provider configuration.
   *
   * @param providerUrl The provider URL.
   * @param clientId Client ID to use.
   * @param clientSecret Client Secret to use.
   */
  public OidcProvider(
      String orchestratorUrl,
      KeyStore orchestratorCert,
      String providerUrl,
      String clientId,
      String clientSecret) {
    super(createOidc2Template(providerUrl, clientId, clientSecret));
    this.orchestratorUrl = orchestratorUrl;
    this.orchestratorCert = orchestratorCert;
    configuration =
        ((org.springframework.social.oidc.deep.connect.OidcTemplate) getOAuthOperations())
            .getConfiguration();
  }

  private static org.springframework.social.oidc.deep.connect.OidcTemplate createOidc2Template(
      String providerUrl, String clientId, String clientSecret) {
    RestTemplate tempTemplate = new RestTemplate();
    OidcConfiguration configuration = new OidcConfiguration();
    try {
      configuration =
          tempTemplate.getForObject(
              providerUrl + "/.well-known/openid-configuration", OidcConfiguration.class);
    } catch (Exception e) {
      logger.warn("Error getting OIDC issuer configuration", e);
      logger.warn("Setting default values for endpoints");
      configuration.setIssuer(providerUrl);
      configuration.setAuthorizationEndpoint(providerUrl + "/authorize");
      configuration.setTokenEndpoint(providerUrl + "/token");
      configuration.setUserinfoEndpoint(providerUrl + "/userinfo");
    }
    org.springframework.social.oidc.deep.connect.OidcTemplate oauth2Template =
        new org.springframework.social.oidc.deep.connect.OidcTemplate(
            configuration, clientId, clientSecret);
    oauth2Template.setUseParametersForClientAuthentication(true);
    return oauth2Template;
  }

  public DeepOrchestrator getApi(String accessToken) {
    try {
      return new DeepOrchestratorTemplate(orchestratorUrl, orchestratorCert, configuration, accessToken);
    } catch (Exception e) {
      logger.error("Error reading orchestrator keystore", e);
    }
    return null;
  }
}
