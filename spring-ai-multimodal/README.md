Spring AI Multimedia (with OpenAI GPT-4o)
===
This is a simple demonstration of how to use Spring AI with OpenAI's
GPT-4o model to answer questions about an image. The image provided
is a weather forecast image in the project at
`src/main/resources/static/forecast.jpg`.

Before running the application, you'll need to obtain an API key for 
OpenAI's API. Go to https://openai.com/, sign up for an account, and 
generate an API key. Once you have an API key, set it to the 
`OPENAI_API_KEY` environment variable. For example:

~~~
% export OPENAI_API_KEY=sk-...
~~~

(The actual API key will be quite a bit longer.)

The easiest way to run the application is to use the Spring Boot Maven
plugin:

~~~
% ./mvnw spring-boot:run
~~~

Once the application starts up, you can begin asking questions by 
POSTing to the `/ask` endpoint. The body of the POST request should be
a simple JSON document with a "question" property.

For example, here's how you might ask a question using the `curl` 
command line tool:

~~~
% curl localhost:8080/ask \
  -H"Content-type: application/json" \
  -d'{"question":"What would be a good day to wash my car?"}'
~~~

Or, if using HTTPie:

~~~
% http :8080/ask question="What would be a good day to wash my car?"
~~~

