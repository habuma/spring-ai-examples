package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Disabled;

@SpringBootTest
// This test fails for lack of a Qdrant vector store. Yes, I could use testcontainers
// to provide it with Qdrant, but this is just a context loading test, so it's not
// really worth it.
@Disabled
class DemoApplicationTests {

    @Test
    fun contextLoads() {
    }

}
