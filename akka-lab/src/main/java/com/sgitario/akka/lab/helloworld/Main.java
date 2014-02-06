package com.sgitario.akka.lab.helloworld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.sgitario.akka.lab.helloworld.actors.GreeterActor;
import com.sgitario.akka.lab.helloworld.actors.HelloWorldActor;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Create the '' actor system
        final ActorSystem system = ActorSystem.create("lab");

        // Create the 'helloworld' actor
        ActorRef helloworld = system.actorOf(Props.create(HelloWorldActor.class), "helloWorld");
        System.out.println("Path: " + helloworld.path().toString());
        Thread.sleep(200);
        
        helloworld.tell(GreeterActor.Msg.GOODBYE, ActorRef.noSender());
        Thread.sleep(200);
        
        system.shutdown();
		system.awaitTermination();
    }
}
