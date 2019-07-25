package org.springframework.social.oidc.deep.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oidc.deep.api.DeepOrchestrator;
import org.springframework.social.oidc.deep.api.OidcConfiguration;
import org.springframework.social.oidc.deep.api.OidcUserProfile;
import org.springframework.social.support.URIBuilder;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class DeepOrchestratorTemplate extends AbstractOAuth2ApiBinding implements DeepOrchestrator {

  private static final Log logger = LogFactory.getLog(DeepOrchestratorTemplate.class);

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
  public DeepOrchestratorTemplate(
      String orchestratorBaseUrl,
      KeyStore orchestratorCert,
      OidcConfiguration configuration,
      String accessToken)
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    super(accessToken);
    this.configuration = configuration;
    this.baseUrl = URI.create(orchestratorBaseUrl + WS_PATH_DEPLOYMENTS);
    if (orchestratorCert != null) {
      setSslContext(orchestratorCert);
    }
  }

  public void setSslContext(KeyStore cert)
      throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    if (cert != null) {
      SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
      sslContextBuilder.loadTrustMaterial(cert);
      SSLContext context = sslContextBuilder.build();
      HttpComponentsClientHttpRequestFactory requestFactory =
          new HttpComponentsClientHttpRequestFactory();
      requestFactory.setHttpClient(HttpClients.custom().setSslcontext(context).build());
      this.setRequestFactory(requestFactory);
    }
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
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(yamlTopology ,headers);
    return getRestTemplate().postForEntity(baseUrl, entity, String.class);
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
