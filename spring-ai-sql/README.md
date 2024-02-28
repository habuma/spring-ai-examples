Spring AI SQL
===
This is a simple demonstration of how to use Spring AI
to generate queries for a given schema DDL from
English questions and then submit those queries to the
database to get answers.

Before running the application, you'll need to obtain
an API key for OpenAI's API. Go to https://openai.com/,
sign up for an account, and generate an API key. Once
you have an API key, set it to the `OPENAI_API_KEY`
environment variable. For example:

~~~
% export OPENAI_API_KEY=sk-...
~~~

(The actual API key will be quite a bit longer.)

The easiest way to run the application is to use the
Spring Boot Maven plugin:

~~~
% ./mvnw spring-boot:run
~~~

Once the application starts up, you can begin asking
questions by POSTing to the `/sql` endpoint. The body
of the POST request should be a simple JSON document
with a "question" property.

For example, here's how you might ask a question using
the `curl` command line tool:

~~~
% curl localhost:8080/sql \
  -H"Content-type: application/json" \
  -d'{"question":"How many books has Craig Walls written?"}'
~~~

The AI generates and submits the following query to
the database:

~~~
SELECT COUNT(*)
FROM Books b
JOIN Authors a ON b.author_ref = a.id
WHERE a.firstName = 'Craig' AND a.lastName = 'Walls';
~~~

Which should return the following response:

~~~
[{COUNT(*)=4}]
~~~

Using [HTTPie](https://httpie.io/) makes this even
easier:

~~~
% http :8080/sql question="How many books has Craig Walls written?"
~~~

Here are a few other questions you might try:

 - What books has Craig Walls written that have been published by Manning Publications?
 - What distinct authors have written books for Pragmatic Bookshelf?
 - Who wrote 'Build Talking Apps for Alexa'?
 - How many books have Java in the title?
 - What size shoe does Lebron James wear? (returns a response stating that the DDL doesn't support that question)

Note that while the generated SQL is usually correct
and usually works, you might encounter occasions where
the SQL returns nothing when it should return
something. Or worse, the generated SQL isn't valid for
the database.

Also note that although the prompt explicitly forbids
inserts, updates, and deletes, sometimes it allows
them to be generated anyway. That's why the controller
implementation only executes the query if it starts
with "select".
