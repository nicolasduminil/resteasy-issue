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