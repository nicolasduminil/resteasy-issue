package fr.simplex_software.workshop.resteasy_issue.api;

import fr.simplex_software.workshop.resteasy_issue.domain.*;
import fr.simplex_software.workshop.resteasy_issue.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.*;

@ApplicationScoped
@Path("customers")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class CustomerResource
{
  @Inject
  CustomerRepository customerRepository;

  @GET
  public Response getAll()
  {
    return Response.ok().entity(customerRepository.findAll()).build();
  }

  @GET
  @Path("/{id}")
  public Response getCustomer(@PathParam("id") Long id)
  {
    return Response.ok().entity(customerRepository.findCustomerById(id)).build();
  }

  @POST
  public Response create(Customer customer)
  {
    return Response.created(URI.create("/customers/" + customer.getId()))
      .entity(customerRepository.createCustomer(customer)).build();
  }

  @PUT
  public Response update(Customer customer)
  {
    return Response.accepted().entity(customerRepository.updateCustomer(customer)).build();
  }

  @DELETE
  public Response delete(@QueryParam("id") Long customerId)
  {
    customerRepository.deleteCustomer(customerId);
    return Response.noContent().build();
  }
}
