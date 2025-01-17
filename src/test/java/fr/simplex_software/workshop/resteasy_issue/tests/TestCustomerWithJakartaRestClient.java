package fr.simplex_software.workshop.resteasy_issue.tests;

import fr.simplex_software.workshop.resteasy_issue.domain.*;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.junit.jupiter.*;
import org.testcontainers.junit.jupiter.Container;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

public class TestCustomerWithJakartaRestClient extends TestCustomerCommon
{
  @Test
  public void testCreateCustomer()
  {
    Customer customer = new Customer("John", "Doe", "john.doe@email.com", "096-23425");
    try (Client client = ClientBuilder.newClient())
    {
      Response response = client.target(baseURI).path("/customers").request()
        .post(Entity.entity(customer, MediaType.APPLICATION_XML));
      assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
      customer = response.readEntity(Customer.class);
      assertThat(customer).isNotNull();
      assertThat(customer.getId()).isNotNull();
      assertThat(customer.getFirstName()).isEqualTo("John");
      response =  client.target(baseURI).path("/customers").request().get();
      assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
      List<Customer> customers = response.readEntity(new GenericType<List<Customer>>() {});
      assertThat(customers).isNotNull();
      assertThat(customers).hasSize(2);
      assertThat(customers.get(0).getFirstName()).isEqualTo("Mike");
      assertThat(customers.get(1).getFirstName()).isEqualTo("John");
      response.close();
    }
  }
}
