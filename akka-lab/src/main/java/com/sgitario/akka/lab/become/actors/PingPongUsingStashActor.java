package com.sgitario.akka.lab.become.actors;

import com.sgitario.akka.lab.become.utils.PingPongUtils;

import akka.actor.UntypedActorWithStash;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class PingPongUsingStashActor extends UntypedActorWithStash {
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	private int count = 0;
	private PongProcedure pongBecome = new PongProcedure();
	private PingProcedure pingBecome = new PingProcedure();
	private LastProcedure lastBecome = new LastProcedure();
	
	@Override
	public void preStart() throws Exception {
		super.preStart();	
	}
	
	@Override
	public void postStop() {
		super.postStop();
		
		getContext().unbecome();
		unstashAll();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		// Nothing
		
		log.info("onReceive: " + message);
		
		if (message instanceof String) {
			if (((String) message).matches(PingPongUtils.START)) {
				getContext().become(pingBecome);
			} else {
				stash();
			}
		}
	}
	
	private class PingProcedure implements Procedure<Object> {

		@Override
		public void apply(Object message) throws Exception {
			log.info("Ping state");
			
			if (message instanceof String) {
				if (((String) message).matches(PingPongUtils.PING)) {
					log.info(PingPongUtils.PING);
					count += 1;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						//
					}
					
					getContext().become(pongBecome);
					getSelf().tell(PingPongUtils.PONG, getSelf());
				}
				
				if (count > 10) {
					getContext().become(lastBecome);
					
					unstashAll();
				}
			}
		}
		
	}
	
	private class PongProcedure implements Procedure<Object> {

		@Override
		public void apply(Object message) throws Exception {
			log.info("Pong state");
			
			if (message instanceof String) {
				if (((String) message).matches(PingPongUtils.PONG)) {
					log.info(PingPongUtils.PONG);
					count += 1;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						//
					}
					
					getContext().become(pingBecome);
					getSelf().tell(PingPongUtils.PING, getSelf());
				}
			}
		}
		
	}
	
	private class LastProcedure implements Procedure<Object> {

		@Override
		public void apply(Object message) throws Exception {
			log.info("Last state");
			
			if (message instanceof String) {
				if (((String) message).matches(PingPongUtils.LAST)) {
					log.info(PingPongUtils.LAST);
					
					getContext().stop(getSelf());
					getContext().system().shutdown();
				}
			}
		}
		
	}
}