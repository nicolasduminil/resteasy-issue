package fr.simplex_software.workshop.resteasy_issue.tests;

import fr.simplex_software.workshop.resteasy_issue.domain.*;
import io.restassured.common.mapper.*;
import io.restassured.http.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

public class TestCustomerWithRestAssuredAndXml extends TestCustomerCommon
{
  @Test
  public void testCreateCustomer()
  {
    Customer customer = new Customer("Mike", "Doe", "mike.doe@email.com", "096-23419");
    customer = given()
      .log().all()
      .contentType(ContentType.XML)
      .body(customer)
      .when()
      .post("/customers")
      .then()
      .log().all()
      .statusCode(HttpStatus.SC_CREATED)
      .extract().body().as(Customer.class);
    assertThat(customer).isNotNull();
    assertThat(customer.getFirstName()).isEqualTo("Mike");
    List<Customer> customers = given().log().all()
      .accept(ContentType.XML)
      .when().get("/customers").then().log().all()
      .statusCode(HttpStatus.SC_OK)
      .extract().body().as(new TypeRef<List<Customer>>() {});
  }
}
