package org.springframework.social.oidc.connect;

import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.oidc.api.OidcConfiguration;

public class OidcTemplate extends OAuth2Template {

  private OidcConfiguration configuration;

  /**
   * Spring Social template for OIDC authentication.
   *
   * @param configuration Configuration of the provider endpoint.
   * @param clientId Client ID to use.
   * @param clientSecret Client Secret to use.
   */
  public OidcTemplate(OidcConfiguration configuration, String clientId, String clientSecret) {
    super(
        clientId,
        clientSecret,
        configuration.getAuthorizationEndpoint(),
        configuration.getTokenEndpoint());
    this.configuration = configuration;
  }

  public OidcConfiguration getConfiguration() {
    return configuration;
  }
}
