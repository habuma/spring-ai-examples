Spring AI RAG Example
---
Simple example to load the entire text of a document into a vector store and
then expose an API through which questions can be asked about the document's
content.

> IMPORTANT: This project has been copied into the
[Spring AI Examples repository](https://github.com/habuma/spring-ai-examples) and will no longer be maintained here.
I'm keeping it here for now, but for future updates, see
the Spring AI Examples repository.

Before running the application, you'll need to acquire an OpenAI API key.
Set the API key as an environment variable named `OPENAI_API_KEY`. E.g.,

```
$ export OPENAI_API_KEY=sk-1234567890abcdef1234567890abcdef
```

You'll also need a document for it to load. Set the `app.resource` property
in src/main/resources/application.properties to the resource URL of the
document. It is set to load the Wikipedia page for the game of Chess, but
you can change it to something else. For example:

```
app.resource=file:///Users/someuser/Spring_in_Action_SixthEdition.pdf
```

The resource URL can be a file, classpath, or even an HTTP URL. The file
itself can be any document type supported by Apache Tika, including PDF,
Word, HTML, and more.

Then run the application as you would any Spring Boot application. For
example, using Maven:

```
$ mvn spring-boot:run
```

The first time you run it, it will take a little while to load the document into
the vector store (which will be persisted at /tmp/vectorstore.json). Subsequent
runs will just use the persisted vector store and not try to load the document again.
(This means that if you change the document resource, you'll need to delete
/tmp/vectorstore.json so that it will be reloaded.)

Then you can use `curl` to ask questions:

```
$ curl localhost:8080/ask -H"Content-type: application/json" -d '{"question": "What annotation should I use to create a REST controller?"}'
```

> The question shown in the example was used to ask questions against my book,
[Spring in Action, 6th Edition](https://www.manning.com/books/spring-in-action-sixth-edition?a_aid=habuma&a_bid=f205d999&chan=habuma).
You'll want to ask questions relevant to whatever document you're using.

Or with HTTPie it's a little easier:

```
http :8080/ask question="What annotation should I use to create a REST controller?"
```
