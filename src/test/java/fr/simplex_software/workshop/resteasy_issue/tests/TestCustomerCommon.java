package fr.simplex_software.workshop.resteasy_issue.tests;

import org.junit.jupiter.api.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;

import static io.restassured.RestAssured.*;

public abstract class TestCustomerCommon
{
  private static Logger LOG = LoggerFactory.getLogger("orders");

  private static final GenericContainer<?> WILDFLY;

  static
  {
    WILDFLY = new GenericContainer<>("wildfly-bootable/resteasy-issue:local")
      .withExposedPorts(8080)
      .withNetwork(Network.newNetwork())
      .withNetworkAliases("wildfly-network-alias")
      .withLogConsumer(new Slf4jLogConsumer(LOG))
      .waitingFor(Wait.forLogMessage(".*WFLYSRV0025.*", 1));
    WILDFLY.start();
  }
  @BeforeAll
  public static void beforeAll()
  {
    baseURI = "http://" + WILDFLY.getHost() + ":"
      + String.valueOf(WILDFLY.getMappedPort(8080));
  }
}
