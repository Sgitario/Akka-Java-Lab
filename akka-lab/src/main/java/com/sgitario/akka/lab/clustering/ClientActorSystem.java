package com.sgitario.akka.lab.clustering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.AddressFromURIString;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.kernel.Bootable;
import akka.remote.RemoteScope;

import com.sgitario.akka.lab.clustering.actors.ClientActor;
import com.sgitario.akka.lab.clustering.actors.ServerActor;
import com.sgitario.akka.lab.clustering.utils.Command;
import com.typesafe.config.ConfigFactory;

/**
 * Start client:
 * 
 * bin/akka com.sgitario.akka.lab.clustering.ClientActorSystem
 * 
 * @author jhilario
 * 
 */
public class ClientActorSystem implements Bootable {

	private ActorSystem system = ActorSystem.create("ClientSys", ConfigFactory
			.load("clusteringExample.client.conf").getConfig("ClientSys"));
	private LoggingAdapter log = Logging.getLogger(system, this);
	private ActorRef actor;
	private ActorSelection remoteActor;

	/*
	 * Method demonstrates how to get a reference to the ServerActor deployed on
	 * a remote node and how to pass the message to the same. Key here is
	 * system.actorFor()
	 */
	@SuppressWarnings("serial")
	public void remoteActorRefDemo() {

		// create a local actor and pass the reference of the remote actor
		actor = system.actorOf(Props.create(ClientActor.class));

		// prompt the user to enter their name
		log.info("Started chat");
		String text = "";

		do {
			log.info(">");
			// open up standard input
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				text = br.readLine();
				
				Command command = new Command(text);

				actor.tell(command, ActorRef.noSender());

			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			}

		} while (text == ":q");
	}

	/*
	 * Method demonstrates how to create an instance of ServerActor on remote
	 * node and how to pass the message to the same. This method demonstrates
	 * one way to create the server node address Key here is system.actorOf()
	 * 
	 * refer to the ServerActorSystem for information on new server actor
	 * creation identified via hashcode's
	 */
	// @SuppressWarnings("serial")
	// public void remoteActorCreationDemo1() {
	// log.info("Creating a actor using remote deployment mechanism");
	//
	// // create the address object that points to the remote server
	// Address addr = new Address("akka", "ServerSys", "127.0.0.1", 2552);
	//
	// // creating the ServerActor on the specified remote server
	// final ActorRef serverActor =
	// system.actorOf(Props.create(ServerActor.class)
	// .withDeploy(new Deploy(new RemoteScope(addr))));
	//
	// final ActorSelection serverActorSel =
	// system.actorSelection(serverActor.path());
	//
	// // create a local actor and pass the reference of the remote actor
	// actor = system.actorOf(Props.create(new Creator<ClientActor>() {
	// public ClientActor create() {
	// return new ClientActor(serverActorSel);
	// }
	// }));
	// // send a message to the local client actor
	// actor.tell("Start-RemoteActorCreationDemo1", ActorRef.noSender());
	// }

	/*
	 * Method demonstrates how to create an instance of ServerActor on remote
	 * node and how to pass the message to the same. This method demonstrates an
	 * alternate way to create the server node address Key here is
	 * system.actorOf()
	 * 
	 * Refer to the ServerActorSystem for information on new server actor
	 * creation identified via hashcode's
	 */
	// @SuppressWarnings("serial")
	// public void remoteActorCreationDemo2() {
	// log.info("Creating a actor with remote deployment");
	//
	// // alternate way to create the address object that points to the remote
	// // server
	// Address addr = AddressFromURIString
	// .parse("akka://ServerSys@127.0.0.1:2552");
	//
	// // creating the ServerActor on the specified remote server
	// final ActorRef serverActor =
	// system.actorOf(Props.create(ServerActor.class)
	// .withDeploy(new Deploy(new RemoteScope(addr))));
	//
	// final ActorSelection serverActorSel =
	// system.actorSelection(serverActor.path());
	//
	// // create a local actor and pass the reference of the remote actor
	// actor = system.actorOf(Props.create(new Creator<ClientActor>() {
	// public ClientActor create() {
	// return new ClientActor(serverActorSel);
	// }
	// }));
	//
	// // send a message to the local client actor
	// actor.tell("Start-RemoteActorCreationDemo2", ActorRef.noSender());
	// }

	/*
	 * Method demonstrates the way to create an actor using remote deployment
	 * but the address is not hard coded. The deployment information is picked
	 * from the application.conf akka{ actor { deployment { /remoteServerActor {
	 * remote = "akka://ServerSys@127.0.0.1:2552" } } } } The deployment name
	 * "remoteServerActor" is passed as the parameter to the system.actorOf()
	 * method
	 * 
	 * This option allows to move the actor creation in an existing application
	 * from local node to any remote node
	 * 
	 * Refer to the ServerActorSystem for information on new server actor
	 * creation identified via hashcode's
	 */
	// @SuppressWarnings("serial")
	// public void remoteActorCreationDemo3() {
	// log.info("Creating a actor with remote deployment");
	//
	// // creating the ServerActor on the specified remote server
	// final ActorRef serverActor =
	// system.actorOf(Props.create(ServerActor.class),"remoteServerActor");
	// final ActorSelection serverActorSel =
	// system.actorSelection(serverActor.path());
	//
	// // create a local actor and pass the reference of the remote actor
	// actor = system.actorOf(Props.create(new Creator<ClientActor>() {
	// public ClientActor create() {
	// return new ClientActor(serverActorSel);
	// }
	// }));
	// // send a message to the local client actor
	// actor.tell("Start-RemoteActorCreationDemo3", ActorRef.noSender());
	// }
	//
	public void shutdown() {

		log.info("Sending PoisonPill to ServerActorSystem");
		remoteActor.tell(akka.actor.PoisonPill.getInstance(),
				ActorRef.noSender());

		log.info("Shutting down the ClientActorSystem");
		system.shutdown();
	}

	public void startup() {
	}

	public static void main(String[] args) {

		ClientActorSystem cAS = new ClientActorSystem();
		cAS.remoteActorRefDemo();
		// cAS.remoteActorCreationDemo1();
		// cAS.remoteActorCreationDemo2();
		// cAS.remoteActorCreationDemo3();
		// cAS.shutdown();
	}

}
