Spring AI RAG and Conversations Example
---
Simple example to load the entire text of a document into a vector store and
then expose an API through which questions can be asked about the document's
content. It also shows how to use chat memory to enable conversational
interactions.

Before running the application, you'll need to acquire an OpenAI API key. Set
the API key as an environment variable named `OPENAI_API_KEY`. E.g.,

```
export OPENAI_API_KEY=sk-1234567890abcdef1234567890abcdef
```

You'll also need a document for it to load. Set the `app.resource` property in
src/main/resources/application.properties to the resource URL of the document.
It is set to load the 2024 NFL Rulebook from src/main/resources. You'll need
to download that PDF for yourself from https://operations.nfl.com/the-rules/nfl-rulebook
and copy it into the src/main/resources directory.

Optionally, you can place any PDF you like in src/main/resources and set the
`app.resource` property to point to it. For example, to set it to load the
the PDF of Spring in Action, 6th Edition, you would set it like this:

```
app.resource=file:///Users/someuser/Spring_in_Action_SixthEdition.pdf
```

The resource URL can be a file, classpath, or even an HTTP URL. The file itself
can be any document type supported by Apache Tika, including PDF, Word, HTML,
and more.

The project takes advantage of Spring Boot's Docker Compose support to start a
handful of services, including the Chroma vector database. Be sure to have
Docker Desktop running before starting the application.

Run the application as you would any Spring Boot application. For example, using
Gradle:

```
./gradlew bootRun
```

Or with Maven:

```
./mvnw spring-boot:run
```

The first time you run it, it will take a little while to load the document into
the vector store (which is a Chroma database in this example). Wait for an entry
from `LoaderConfig` in the logs that says "Documents loaded into vector store".

Then you can use `curl` to ask questions:

```
curl localhost:8080/ask -H"Content-type: application/json" \
  -d '{"question": "What is roughing the passer?"}'
```

Or with HTTPie it's a little easier:

```
http :8080/ask question="What is roughing the passer?"
```

You can then ask a followup question and the application will remember the
context of the conversation:

```
curl localhost:8080/ask -H"Content-type: application/json" \
  -d '{"question": "What is the penalty for that?"}'
```

Or with HTTPie:

```
http :8080/ask question="What is the penalty for that?"
```

By default, the application uses a conversation ID of "defaultConversation".
To specify a different conversation ID, specify it via the "X_CONV_ID" header:

```
curl localhost:8080/ask -H"Content-type: application/json" \
  -H"X_CONV_ID: user1" \
  -d '{"question": "What is roughing the passer?"}'
  
curl localhost:8080/ask -H"Content-type: application/json" \
  -H"X_CONV_ID: user1" \
  -d '{"question": "What is the penalty for that?"}'
```

Or with HTTPie:

```
http :8080/ask question="What is roughing the passer?" X_CONV_ID:user1

http :8080/ask question="What is the penalty for that?" X_CONV_ID:user1
```

