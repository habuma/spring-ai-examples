Summarizing text with Spring AI
===
This example shows how to summarize a document using Spring AI.

Before running the application, you'll need to obtain an API key for OpenAI's
API. Go to https://openai.com/, sign up for an account, and generate an API key.
Once you have an API key, set it to the `OPENAI_API_KEY` environment variable.
For example:

~~~
% export OPENAI_API_KEY=sk-...
~~~

(The actual API key will be quite a bit longer.)

The easiest way to run the application is to use the Spring Boot Maven plugin:

~~~
% ./mvnw spring-boot:run
~~~

Once the application starts up, you can submit a document for summarization by
posting the document to the `/summarize` endpoint. For example, here's how you
might submit a document for summarization using the `curl` command line tool:

~~~
$ curl --form file='@mydocument.pdf` http://localhost:8080/summarize
~~~

Or, if using HTTPie:

~~~
$ http -f POST localhost:8080/summarize \
      file@'mydocument.pdf;type=application/pdf'
~~~
