package com.sgitario.akka.lab.clustering;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.kernel.Bootable;

import com.sgitario.akka.lab.clustering.actors.ServerActor;
import com.typesafe.config.ConfigFactory;

/**
 * Start client:
 * 
 * bin/akka com.sgitario.akka.lab.clustering.ServerActorSystem
 * @author jhilario
 *
 */
public class ServerActorSystem implements Bootable {

	private LoggingAdapter log = null;
	private ActorSystem system;

	/*
	 * default constructor
	 */
	public ServerActorSystem() {
		// load the configuration
		system = ActorSystem.create("ServerSys", ConfigFactory.load("clusteringExample.server.conf")
				.getConfig("ServerSys"));
		log = Logging.getLogger(system, this);
		
		// create the actor
		ActorRef actor = system.actorOf(Props.create(ServerActor.class),
				"serverActor");
		
		log.info("Server path: " + actor.path());
	}

	public void shutdown() {
		log.info("Shutting down the ServerActorSystem");

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		new ServerActorSystem();

	}

}
