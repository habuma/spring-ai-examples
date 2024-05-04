# Vector Store Loader
A Spring Boot application that loads a vector store from files dropped into
a directory.

## Quick Start
You'll need Docker installed and running on your machine. When the application
starts, it uses a docker-compose.yaml file to start a ChromaDB instance.

You'll also need an OpenAI API key. You can get one by signing up at
https://platform.openai.com/ and creating an API key. Set it to an environment
variable named `OPENAI_API_KEY` before running the application.

Start the application with either Gradle...

```shell
./gradlew bootRun
```

...or with Maven...

```shell
./mvnw spring-boot:run
```

Then copy documents (PDF, text, Excel, etc) into the `/tmp/dropoff` directory
to have them loaded into Chroma. If that folder doesn't yet exist, it will be
created for you, so long as filesystem permissions allow it. You can change
the location by editing `src/main/resources/application.yml` and adjusting
the `file.supplier.directory` property.

Note that larger documents take more time to load, so watch the logs to know
when the document has been completely loaded.

## Technical Details

### Spring MVC and Keeping the App Running

The application includes the Spring MVC starter because Spring AI depends on
it. But it does not expose an API and the server is allowed to run for no
other reason than to keep the application running after the `ApplicationRunner`
bean has completed. The server is configured to run on a randomly available
high number port, so there shouldn't be any conflict with any other application
using the same port.

Alternatively, you could add the scheduler starter and
and then define an empty method with `@Scheduled` to keep the application
running. But since Spring AI needs Spring MVC anyway, it's just as easy to
have the Tomcat server keep the application alive.

### ChromaDB

The application using Spring Boot's Docker Compose support to start ChromaDB
from an application.yaml file in the project's root directory. This is
perfectly suitable for development and demo purposes. In a real-world scenario,
you would likely have an existing ChromaDB instance to connect to and wouldn't
need to start it from the application. In that case, simply delete the
docker-compose.yaml file and remove the Spring Boot Docker Compose starter
from the build.gradle file.

If you allow the application to start ChromaDB from the Docker Compose file,
it will be listening on localhost, port 8000. This allows other applications,
such as a Spring AI-enabled RAG application to use the same ChromaDB instance
to search for similar documents based on a query. If you're using Spring AI's
ChromaDB starter, autoconfiguration will create a `VectorStore` bean that
references the ChromaDB instance on localhost port 8000, so you shouldn't need
to explicitly configure the ChromaDB host and port.

### Spring Function Catalog

The application leverages the Spring Cloud Function and the Spring Function
Catalog to define the document loading pipeline. The pipeline is defined
in `src/main/resources/application.yml` as follows:

```yaml
spring:
  cloud:
    function:
      definition: fileSupplier|vectorStoreLoaderConsumer
```

The `fileSupplier` function is provided by the Spring Function Catalog and
is configured in `src/main/resources/application.yml` to watch for files to
appear in the `/etc/dropoff` directory. When a file appears, the function
reads the file and sends it to the `vectorStoreLoaderConsumer` function.

The `vectorStoreLoaderConsumer` function is defined in the `VectorStoreLoader`
class. It receives a `Flux<Message<byte[]>>` from which it extracts the source
filename from the message headers and the file content from the message body.
Using Spring AI's `TikaDocumentLoader` to create a `Document` from the bytes,
Spring AI's `TokenTextSplitter` to split the document into smaller documents,
then loads the smaller documents into the `VectorStore` bean (which is, in fact,
a `ChromaVectorStore`).

The function is kicked off by the `ApplicationRunner` bean, which looks up
the composed function and calls its `run()` method. From that point, the
`fileSupplier` function is watching for files to appear in the `/etc/dropoff`
directory.
