package org.springframework.social.oidc.deep.api;

import org.springframework.http.ResponseEntity;
import org.springframework.social.ApiBinding;

public interface DeepOrchestrator extends ApiBinding {

  /**
   * Returns the profile of the logged user.
   * @return The profile of the logged user.
   */
  OidcUserProfile getProfile();

  /**
   * Gets a list of deployments of the logged user.
   * @return The list of deployments in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callGetDeployments();

  /**
   * Deploys a template in the orchestrator.
   * @param yamlTopology The yaml topology to deploy in plain text.
   * @return The operation result in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callDeploy(String yamlTopology);

  /**
   * Gets the status of a deployment.
   * @param deploymentId The deployment identifier.
   * @return The deployment status in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callDeploymentStatus(String deploymentId);

  /**
   * Undeploys a deployment.
   * @param deploymentId The deployment identifier.
   * @return The operation result in plain text. It must be parsed by the calling client.
   */
  ResponseEntity<String> callUndeploy(String deploymentId);
}
