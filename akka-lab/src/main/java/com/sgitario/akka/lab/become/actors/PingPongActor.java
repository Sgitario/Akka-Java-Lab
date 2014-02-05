package com.sgitario.akka.lab.become.actors;

import com.sgitario.akka.lab.become.utils.PingPongUtils;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class PingPongActor extends UntypedActor {
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	private int count = 0;
	private PongProcedure pongBecome = new PongProcedure();
	private PingProcedure pingBecome = new PingProcedure();
	
	@Override
	public void preStart() throws Exception {
		super.preStart();
		
		// Ping state
		getContext().become(pingBecome);
	}
	
	@Override
	public void postStop() throws Exception {
		super.postStop();
		
		getContext().unbecome();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		// Nothing
	}
	
	private class PingProcedure implements Procedure<Object> {

		@Override
		public void apply(Object message) throws Exception {
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
					getContext().stop(getSelf());
					getContext().system().shutdown();
				}
			}
		}
		
	}
	
	private class PongProcedure implements Procedure<Object> {

		@Override
		public void apply(Object message) throws Exception {
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
}