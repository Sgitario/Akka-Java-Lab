package com.sgitario.akka.lab.helloworld;

import scala.concurrent.duration.Duration;
import akka.actor.*;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sgitario.akka.lab.helloworld.actors.HelloWorldActor;


public class HelloAkkaTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        system.shutdown();
        system.awaitTermination(Duration.create("10 seconds"));
    }

    @Test
    public void testSetGreeter() {
        new JavaTestKit(system) {{
            final TestActorRef<HelloWorldActor> greeter =
                TestActorRef.create(system, Props.create(HelloWorldActor.class), "helloWorld");

            new Within(duration("10 seconds")) {
                protected void run() {
                	Assert.assertEquals("Hello World!", greeter.underlyingActor().getMessage());
                }
            };
            
            
        }};
    }
}
