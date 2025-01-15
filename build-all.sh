#!/bin/sh
cd ./prompts-and-output-parsers
./mvnw -q test
cd ..
cd ./spring-ai-evaluators
./mvnw -q test
cd ..
cd ./spring-ai-functions
./mvnw -q test
cd ..
cd ./spring-ai-gemma
./mvnw -q test
cd ..
cd ./spring-ai-image-gen
./mvnw -q test
cd ..
cd ./spring-ai-kotlin
./mvnw -q test
cd ..
cd ./spring-ai-kotlin-rag
./mvnw -q test
cd ..
cd ./spring-ai-mcp
./gradlew -q test
cd ..
# cd ./spring-ai-mcp-fun
# ./gradlew -q test
# cd ..
cd ./spring-ai-multimodal
./mvnw -q test
cd ..
cd ./spring-ai-multimodal-audio
./mvnw -q test
cd ..
cd ./spring-ai-qaadvisor
./mvnw -q test
cd ..
cd ./spring-ai-rag-chat
./mvnw -q test
cd ..
cd ./spring-ai-rag-example
./mvnw -q test
cd ..
cd ./spring-ai-sql
./mvnw -q test
cd ..
cd ./spring-ai-summarizer
./mvnw -q test
cd ..
cd ./vector-store-loader
./mvnw -q test
