package org.springframework.social.oidc.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oidc.api.Oidc;
import org.springframework.social.oidc.api.OidcConfiguration;
import org.springframework.social.oidc.api.OidcUserProfile;
import org.springframework.social.support.URIBuilder;

import java.net.URI;

public class OidcTemplate extends AbstractOAuth2ApiBinding implements Oidc {

  private static final Log logger = LogFactory.getLog(OidcTemplate.class);

  private OidcConfiguration configuration;

  private URI baseUrl;
  /** Web service path for deployments operations; It is appended to the orchestrator endpoint. */
  public static final String WS_PATH_DEPLOYMENTS = "/deployments";

  /**
   * Creates a new OIDC Template based on the OIDC endpoint configuration.
   *
   * @param configuration Configuration of the OIDC endpoint
   * @param accessToken Obtained access token
   */
  public OidcTemplate(
      String orchestratorBaseUrl, OidcConfiguration configuration, String accessToken) {
    super(accessToken);
    this.configuration = configuration;
    this.baseUrl = URI.create(orchestratorBaseUrl + WS_PATH_DEPLOYMENTS);
  }

  public OidcUserProfile getProfile() {
    return getRestTemplate()
        .getForObject(configuration.getUserinfoEndpoint(), OidcUserProfile.class);
  }

  public ResponseEntity<String> callGetDeployments() {
    URIBuilder builder = URIBuilder.fromUri(baseUrl);
    builder.queryParam("createdBy", "me");

    return getRestTemplate().getForEntity(builder.build().toString(), String.class);
  }

  public ResponseEntity<String> callDeploy(String yamlTopology) {
    return getRestTemplate().postForEntity(baseUrl, yamlTopology, String.class);
  }

  public ResponseEntity<String> callDeploymentStatus(String deploymentId) {
    return getRestTemplate()
        .getForEntity(URI.create(baseUrl.toString() + "/" + deploymentId), String.class);
  }

  public ResponseEntity<String> callUndeploy(String deploymentId) {
    RequestEntity<Void> requestEntity =
        new RequestEntity<Void>(
            HttpMethod.DELETE, URI.create(baseUrl.toString() + "/" + deploymentId));
    return getRestTemplate().exchange(requestEntity, String.class);
  }
}
