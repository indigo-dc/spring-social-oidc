#!/usr/bin/groovy

@Library(['github.com/indigo-dc/jenkins-pipeline-library@1.3.6']) _

pipeline {
    agent {
       label 'java'
    }
    
    stages {

        stage('Code fetching') {
            steps {
                     checkout([$class: 'GitSCM', branches: [[name: 'add_jenkinsfile']],  extensions: [[$class: 'CleanCheckout']], userRemoteConfigs: [[url: 'https://github.com/indigo-dc/spring-social-oidc.git']]])
            }
        }

	    stage('Build Spring OIDC') {
            steps {
                    MavenRun('-U clean package')
            }
        }

        stage('Results') {
            steps {
                archiveArtifacts 'target/*.jar'
            }    
        }
        
        stage( 'Publish' ) {
            steps {
                nexusPublisher nexusInstanceId: 'nexus-server', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'target/spring-social-oidc-deep-1.3.jar']], mavenCoordinate: [artifactId: 'spring-social-oidc-deep', groupId: 'org.springframework.social', packaging: 'war', version: '1.3']]]
            }    
        }
    }
}
