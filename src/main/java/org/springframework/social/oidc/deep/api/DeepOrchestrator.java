package org.springframework.social.oidc.deep.api;

import org.springframework.http.ResponseEntity;
import org.springframework.social.ApiBinding;

public interface DeepOrchestrator extends ApiBinding {

  OidcUserProfile getProfile();

  ResponseEntity<String> callGetDeployments();

  ResponseEntity<String> callDeploy(String yamlTopology);

  ResponseEntity<String> callDeploymentStatus(String deploymentId);

  ResponseEntity<String> callUndeploy(String deploymentId);
}
