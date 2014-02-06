package com.sgitario.akka.lab.helloworld;

import akka.actor.ActorSystem;
import akka.actor.Props;

import com.sgitario.akka.lab.helloworld.actors.HelloWorldActor;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Create the '' actor system
        final ActorSystem system = ActorSystem.create("lab");

        // Create the 'helloworld' actor
        system.actorOf(Props.create(HelloWorldActor.class), "helloWorld");
        
        Thread.sleep(200);
        
        system.shutdown();
		system.awaitTermination();
    }
}
