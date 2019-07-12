package org.springframework.social.oidc.deep.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.oidc.deep.api.DeepOrchestrator;
import org.springframework.social.oidc.deep.api.OidcUserProfile;

public class OidcAdapter implements ApiAdapter<DeepOrchestrator> {

  public boolean test(DeepOrchestrator api) {
    return api.isAuthorized() && api.getProfile() != null;
  }

  /**
   * Sets the connection values for this OIDC connection.
   *
   * @param api The API to get the user profile from.
   * @param values The user profile values to set from the API.
   */
  public void setConnectionValues(DeepOrchestrator api, ConnectionValues values) {
    OidcUserProfile profile = api.getProfile();
    values.setProviderUserId(profile.getSub());
    values.setDisplayName(profile.getGivenName() + " " + profile.getFamilyName());
  }

  /**
   * Returns the user profile from the API.
   *
   * @param api The API to request.
   * @return The user profile.
   */
  public UserProfile fetchUserProfile(DeepOrchestrator api) {
    OidcUserProfile profile = api.getProfile();
    return new UserProfileBuilder()
        .setId(profile.getSub())
        .setUsername(profile.getPreferredUsername())
        .setEmail(profile.getEmail())
        .setFirstName(profile.getGivenName())
        .setLastName(profile.getFamilyName())
        .build();
  }

  public void updateStatus(DeepOrchestrator api, String message) {}
}
