package org.springframework.social.oidc.api;

import org.springframework.social.ApiBinding;

public interface Oidc extends ApiBinding {

  OidcUserProfile getProfile();
}
