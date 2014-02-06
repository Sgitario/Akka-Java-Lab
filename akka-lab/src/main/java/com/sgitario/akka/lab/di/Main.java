package com.sgitario.akka.lab.di;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;

import com.sgitario.akka.lab.di.utils.MessageEnum;
import com.sgitario.akka.lab.di.utils.SpringActorProducer;

public class Main {
	public static void main(String[] args) {
		// Spring context
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.scan("com.sgitario.akka.lab.di");
		appContext.refresh();

		ActorSystem system = appContext.getBean(ActorSystem.class);
		ActorRef counter = system.actorOf(Props.create(
				SpringActorProducer.class, appContext, "CounterActor"));

		// tell it to count three times
		counter.tell(MessageEnum.Tick, ActorRef.noSender());
		counter.tell(MessageEnum.Tick, ActorRef.noSender());
		counter.tell(MessageEnum.Tick, ActorRef.noSender());

		// print the result
		FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
		Future<Object> result = akka.pattern.Patterns.ask(counter, MessageEnum.Get,
				Timeout.durationToTimeout(duration));
		try {
			System.out.println("Got back " + Await.result(result, duration));
		} catch (Exception e) {
			System.err.println("Failed getting result: " + e.getMessage());
		} finally {
			system.shutdown();
			system.awaitTermination();
		}
	}

}
