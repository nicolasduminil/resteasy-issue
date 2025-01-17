# A RESTeasy issue

This is a test project to be submitted to RedHat in order to reproduce an issue.
It exposes a simple RESTful endpoint which CRUDes in a H2 database `Customer` entities.
When trying to get the list of all existent customers, the following exception is raised:

    RESTEASY002005: Failed executing GET /customers: org.jboss.resteasy.core.NoMessageBodyWriterFoundFailure: Could not find MessageBodyWriter for response object of type: java.util.ArrayList of media type: application/xml;charset=UTF-8

For some reason, RESTeasy doesn't manage to serialize a `List<Customer>`.

To reproduce:

    $ git clone https://github.com/nicolasduminil/resteasy-issue.git
    $ cd resteasy-issue
    $ mvn -DskipTests package
    $ mvn test

During the packaging operation, the Docker image `wildfly-bootable/resteasy-issue:local` is created.
The, in the test phase, this image is executed by `testcontainers` and the REST assured test
is executed. It will successfully create a new customer but, trying to get it back,
the mentioned exception is raised.

I expect that RESTeasy be ble to marshal/unmarshal lists without requiring customized 
`MessageBodyWriter/MessageBodyReader` implementations.

# Update

The mentioned exception was raised because the endpoint was returning an inappropriate result.

    ...
    return Response.ok().entity(customerRepository.findAll()).build();
    ...

should be modified to:

    return Response.ok().entity(new GenericEntity<List<Customer>>(customerRepository.findAll()) {}).build();

Once this modification done, another exception is raised:

    java.lang.RuntimeException: JAXB does not support typejava.util.List<fr.simplex_software.workshop.resteasy_issue.domain.Customer>
      at io.restassured.path.xml.mapper.factory.DefaultJakartaEEObjectMapperFactory.create(DefaultJakartaEEObjectMapperFactory.java:33)
      at io.restassured.path.xml.mapper.factory.DefaultJakartaEEObjectMapperFactory.create(DefaultJakartaEEObjectMapperFactory.java:27)
      at io.restassured.common.mapper.factory.ObjectMapperFactory$create.call(Unknown Source)

The stack trace shows that the exception is raised by the RESTassured library, which means that it might not be a RESTeasy issue.

In order to make sure, I added a Jakarta REST Client test which works as expected, proving that the issue is exclussively related to RESTassure.
And since the endpoint initially produced and consumed XML, I modified it such that to produce/consume JSON as well. Then I added
a 3rd test which uses RESTassured as well, but this time with JSON payloads instead of XML. This test works as expected also.

So, to resume:

| Test class name | Client | Payload | Status |
|-----------------|--------|---------|--------|
| TestCustomerWithRestAssuredAndXml | RESTassured | XML | Failed |
| TestCustomerWithRestAssuredAndJson | RESTassured | JSON | Passed |
| TestCustomerWithJakartaRestClientAndXml | Jakarta REST Client | XML | Passed |

I'll file an issue with the RESTassured support, if any.