package org.springframework.social.oidc.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oidc.api.Oidc;

public class OidcConnectionFactory extends OAuth2ConnectionFactory<Oidc> {

  public OidcConnectionFactory(String baseUrl, String clientId, String clientSecret) {
    super("oidc", new OidcProvider(baseUrl, clientId, clientSecret), new OidcAdapter());
  }
}
