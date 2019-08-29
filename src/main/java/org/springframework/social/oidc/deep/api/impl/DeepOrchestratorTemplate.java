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
import java.security.cert.Certificate;

import javax.net.ssl.SSLContext;

public class DeepOrchestratorTemplate extends AbstractOAuth2ApiBinding implements DeepOrchestrator {

  private static final Log logger = LogFactory.getLog(DeepOrchestratorTemplate.class);

  private OidcConfiguration configuration;

  private KeyStore keystore;

  /** Web service path for deployments operations; It is appended to the orchestrator endpoint. */
  public static final String WS_PATH_DEPLOYMENTS = "/deployments";

  /**
   * Creates a new OIDC Template based on the OIDC endpoint configuration.
   *
   * @param configuration Configuration of the OIDC endpoint
   * @param accessToken Obtained access token
   */
  public DeepOrchestratorTemplate(
      KeyStore orchestratorCert, OidcConfiguration configuration, String accessToken)
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    super(accessToken);
    this.keystore = orchestratorCert;
    this.configuration = configuration;
    if (orchestratorCert != null) {
      setSslContext(orchestratorCert);
    }
  }

  /**
   * When the orchestrator is using an invalid certificate, this method can be called to accept the
   * certificate.
   *
   * @param cert A JKS keystore containing the orchestrator certificate.
   * @throws KeyStoreException The keystore is invalid.
   * @throws NoSuchAlgorithmException A problem occurred opening the keystore.
   * @throws KeyManagementException A problem occurred opening the keystore.
   */
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

  private String baseUrl(String orchestrarorUrl) {
    return orchestrarorUrl + WS_PATH_DEPLOYMENTS;
  }

  public void addCertificate(String alias, Certificate cert)
      throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
    if (this.keystore != null) {
      this.keystore.setCertificateEntry(alias, cert);
      setSslContext(this.keystore);
    }
  }

  public void removeCertificate(String alias)
      throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
    if (this.keystore != null) {
      this.keystore.deleteEntry(alias);
      setSslContext(this.keystore);
    }
  }

  /**
   * Returns the profile of the logged user.
   *
   * @return The profile of the logged user.
   */
  public OidcUserProfile getProfile() {
    return getRestTemplate()
        .getForObject(configuration.getUserinfoEndpoint(), OidcUserProfile.class);
  }

  /**
   * Gets a list of deployments of the logged user.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @return The list of deployments in plain text. It must be parsed by the calling client.
   */
  public ResponseEntity<String> callGetDeployments(String orchestrarorUrl) {
    URIBuilder builder = URIBuilder.fromUri(baseUrl(orchestrarorUrl));
    builder.queryParam("createdBy", "me");

    return getRestTemplate().getForEntity(builder.build().toString(), String.class);
  }

  /**
   * Deploys a template in the orchestrator.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param yamlTopology The yaml topology to deploy in plain text.
   * @return The operation result in plain text. It must be parsed by the calling client.
   */
  public ResponseEntity<String> callDeploy(String orchestrarorUrl, String yamlTopology) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(yamlTopology, headers);
    return getRestTemplate().postForEntity(baseUrl(orchestrarorUrl), entity, String.class);
  }

  /**
   * Gets the status of a deployment.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param deploymentId The deployment identifier.
   * @return The deployment status in plain text. It must be parsed by the calling client.
   */
  public ResponseEntity<String> callDeploymentStatus(String orchestrarorUrl, String deploymentId) {
    return getRestTemplate()
        .getForEntity(
            URI.create(baseUrl(orchestrarorUrl).toString() + "/" + deploymentId), String.class);
  }

  /**
   * Undeploys a deployment.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param deploymentId The deployment identifier.
   * @return The operation result in plain text. It must be parsed by the calling client.
   */
  public ResponseEntity<String> callUndeploy(String orchestrarorUrl, String deploymentId) {
    RequestEntity<Void> requestEntity =
        new RequestEntity<Void>(
            HttpMethod.DELETE,
            URI.create(baseUrl(orchestrarorUrl).toString() + "/" + deploymentId));
    return getRestTemplate().exchange(requestEntity, String.class);
  }

  /**
   * Gets the template description associated to a deployment.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param deploymentId The deployment identifier.
   * @return The deployment template in plain text. It must be parsed by the calling client.
   */
  public ResponseEntity<String> callGetTemplate(String orchestrarorUrl, String deploymentId) {
    return getRestTemplate()
        .getForEntity(
            URI.create(baseUrl(orchestrarorUrl).toString() + "/" + deploymentId + "/template"),
            String.class);
  }
}
