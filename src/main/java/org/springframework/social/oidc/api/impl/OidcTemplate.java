package org.springframework.social.oidc.api.impl;

import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oidc.api.Oidc;
import org.springframework.social.oidc.api.OidcConfiguration;
import org.springframework.social.oidc.api.OidcUserProfile;

public class OidcTemplate extends AbstractOAuth2ApiBinding implements Oidc {

  private OidcConfiguration configuration;

  public OidcTemplate(OidcConfiguration configuration, String accessToken) {
    super(accessToken);
    this.configuration = configuration;
  }

  public OidcUserProfile getProfile() {
    return getRestTemplate()
        .getForObject(configuration.getUserinfoEndpoint(), OidcUserProfile.class);
  }
}
