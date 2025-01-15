package fr.simplex_software.workshop.resteasy_issue.repository;

import fr.simplex_software.workshop.resteasy_issue.domain.*;
import jakarta.enterprise.context.*;
import jakarta.persistence.*;
import jakarta.transaction.*;
import jakarta.ws.rs.*;

import java.util.*;

@ApplicationScoped
public class CustomerRepository
{
  @PersistenceContext(unitName = "orders")
  EntityManager entityManager;

  public List<Customer> findAll()
  {
    return entityManager.createNamedQuery("Customer.findAll", Customer.class)
      .getResultList();
  }

  public Customer findCustomerById(Long id)
  {
    Customer customer = entityManager.find(Customer.class, id);
    if (customer == null)
    {
      throw new WebApplicationException("Customer with id of " + id + " does not exist.", 404);
    }
    return customer;
  }

  @Transactional
  public Customer updateCustomer(Customer customer)
  {
    Customer customerToUpdate = findCustomerById(customer.getId());
    customerToUpdate.setFirstName(customer.getFirstName());
    customerToUpdate.setLastName(customer.getLastName());
    customerToUpdate.setEmail(customer.getEmail());
    customerToUpdate.setPhone(customer.getPhone());
    entityManager.merge(customerToUpdate);
    return customerToUpdate;
  }

  @Transactional
  public Customer createCustomer(Customer customer)
  {
    entityManager.persist(customer);
    return customer;
  }

  @Transactional
  public void deleteCustomer(Long customerId)
  {
    Customer c = findCustomerById(customerId);
    entityManager.remove(c);
  }
}
