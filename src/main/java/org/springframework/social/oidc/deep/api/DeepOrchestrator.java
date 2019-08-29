package org.springframework.social.oidc.deep.api;

import org.springframework.http.ResponseEntity;
import org.springframework.social.ApiBinding;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;

public interface DeepOrchestrator extends ApiBinding {

  /**
   * Adds a certificate to the existing keystore in memory.
   *
   * @param alias The alias to apply to this certificate. It must be unique. If the alias already
   *     exists, the existing certificate will be replaced.
   * @param cert The certificate to add.
   * @throws KeyStoreException Thrown if something go wrong.
   */
  void addCertificate(String alias, Certificate cert)
      throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException;

  /**
   * Removes a certificate from the existing keystore in memory.
   *
   * @param alias The alias of the certificate to remove.
   * @throws KeyStoreException Thrown if something go wrong.
   */
  void removeCertificate(String alias)
      throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException;

  /**
   * Returns the profile of the logged user.
   *
   * @return The profile of the logged user.
   */
  OidcUserProfile getProfile();

  /**
   * Gets a list of deployments of the logged user.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @return The list of deployments in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callGetDeployments(String orchestrarorUrl);

  /**
   * Deploys a template in the orchestrator.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param yamlTopology The yaml topology to deploy in plain text.
   * @return The operation result in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callDeploy(String orchestrarorUrl, String yamlTopology);

  /**
   * Gets the status of a deployment.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param deploymentId The deployment identifier.
   * @return The deployment status in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callDeploymentStatus(String orchestrarorUrl, String deploymentId);

  /**
   * Undeploys a deployment.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param deploymentId The deployment identifier.
   * @return The operation result in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callUndeploy(String orchestrarorUrl, String deploymentId);

  /**
   * Gets the template description associated to a deployment.
   *
   * @param orchestrarorUrl The URL of the DEEP orchestrator to contact.
   * @param deploymentId The deployment identifier.
   * @return The deployment template in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callGetTemplate(String orchestrarorUrl, String deploymentId);
}
