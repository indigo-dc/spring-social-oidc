# DEEP Orchestrator and OpenID Connect support for spring social #

This project provides integration with the DEEP orchestrator and OpenID connect log-in support through Spring Social to Spring projects.

## Build ##

Maven is needed to build the source code. To build a binary just execute `mvn clean install` and the jar should be in the `target` folder.

## Use in a Spring project ##

1. Add the generated jar to the Spring project classpath either by a dependency management tool like Maven or Gradle or copying the jar directly to the project's classpath.
2. Follow the steps described in [Spring social documentation](https://docs.spring.io/spring-social/docs/1.1.4.RELEASE/reference/htmlsingle) to enable Spring Social in your project and create a SocialConfigurer and adding a `SocialConfigurer` class and add a connection factory like in this snippet:
```java
@Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {

            connectionFactoryConfigurer.addConnectionFactory(
                    new OidcConnectionFactory(certKeystore, issuer, clientId, clientSecret));
    }
```

Where:
- `certKeystore` is the location of a JKS keystore containing orchestrators certificates in case it's self-signed or invalid. If the orchestrator has a valid certificate then this parameter can be null.
- `issuer` is the root URL of the IAM issuer instance 
- `client-id` and `client-sectet` are the application client identifier and secret to use to authenticate through the code workflow.

## Getting access to the DEEP Orchestrator client

In Spring beans and components whose scope is bound to the request, the DEEP orchestrator can be directly injected using the ``@Inject`` or ``@Autowired`` annotations:

```java
@Autowired
private DeepOrchestrator orchestratorClient;
```

In Spring beans whose scope is not bound to the actual request, the DEEP orchestrator can be obtained by the following snippet:

```java
@Autowired
private ConnectionRepository repository;

private DeepOrchestrator getClient() {
    Connection<DeepOrchestrator> connection = repository.findPrimaryConnection(DeepOrchestrator.class);
    DeepOrchestrator deepOrchestrator = connection != null ? connection.getApi() : null;
    return deepOrchestrator;
}
```

## Getting access and refresh tokens from the code ##

It's not recommended to access the IAM token directly and instead it's strongly preferred to implement further operations and services in this plug in and then access them as client with the method defined above, however, if necessary, once configured, you can get the current access and refresh tokens from the current user by:

-  Add a reference to the `ConnectionRepository` object in your class:
```java
@Inject
private ConnectionRepository connRepository;
```

- Access the tokens with the following snippet:
```java
connRepository.getPrimaryConnection(DeepOrchestrator.class).createData()
``` 