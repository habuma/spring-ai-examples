Spring AI Multimedia Audio (with OpenAI GPT-4o-audio-preview)
===
This is a simple demonstration of how to use Spring AI with OpenAI's
GPT-4o-audio-preview model to answer questions from an input audio file
and produce answers in an output audio file.

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
POSTing MP3 files with the spoken questions to the `/ask` endpoint.
The response from that endpoint will be an array of bytes that you
can redirect to a file to save the answer.

For example, here's how you might ask a question using the `curl`
command line tool:

~~~
$ curl --form file='@audiofiles/question1.mp3` \
  http://localhost:8080/ask > answer1.mp3
~~~

Or, if using HTTPie:

~~~
% http -f POST localhost:8080/ask \
  audio@'audio/question1.mp3;type=audio/mp3' > answer1.mp3
~~~

Open the MP3 files using your favorite audio player to hear the
questions and answers.