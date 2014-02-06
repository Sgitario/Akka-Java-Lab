package com.sgitario.akka.lab.inbox;

import com.sgitario.akka.lab.inbox.actors.FamilyActor;
import com.sgitario.akka.lab.inbox.actors.PostManActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		// Create the '' actor system
        final ActorSystem system = ActorSystem.create("lab");

        // Create the 'postman' actor
        ActorRef postMan = system.actorOf(Props.create(PostManActor.class), "postMan");
        ActorRef family = system.actorOf(Props.create(FamilyActor.class, postMan), "family");
        
        // Register family
        postMan.tell(family, ActorRef.noSender());
        
        // Send messages
        postMan.tell("Send order", ActorRef.noSender());
        postMan.tell("Send order", ActorRef.noSender());
        postMan.tell("Send order", ActorRef.noSender());
        
        Thread.sleep(200);
        
        system.shutdown();
	}
}
