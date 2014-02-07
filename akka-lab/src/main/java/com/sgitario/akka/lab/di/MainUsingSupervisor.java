package com.sgitario.akka.lab.di;

import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.util.Timeout;

import com.sgitario.akka.lab.di.utils.MessageEnum;

public class MainUsingSupervisor {

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-supervisor.xml");
		ActorRef actor = (ActorRef) context.getBean("counterSupervision");

		// tell it to count three times
		actor.tell(MessageEnum.Tick, ActorRef.noSender());
		actor.tell(MessageEnum.Tick, ActorRef.noSender());
		actor.tell(MessageEnum.Tick, ActorRef.noSender());

		// print the result
		Thread.sleep(200);
		
		FiniteDuration duration = FiniteDuration.create(10, TimeUnit.MINUTES);
		Future<Object> result = akka.pattern.Patterns.ask(actor, MessageEnum.Get,
				Timeout.durationToTimeout(duration));
		try {
			System.out.println("Got back " + Await.result(result, duration));
		} catch (Exception e) {
			System.err.println("Failed getting result: " + e.getMessage());
		} 
	}

}
