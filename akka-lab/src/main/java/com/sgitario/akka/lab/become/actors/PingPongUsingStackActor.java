package com.sgitario.akka.lab.become.actors;

import com.sgitario.akka.lab.become.utils.PingPongUtils;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class PingPongUsingStackActor extends UntypedActor {
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	private int count = 0;
	private PingPongProcedure pingPongBecome = new PingPongProcedure();
	
	@Override
	public void preStart() throws Exception {
		super.preStart();
		
		getContext().become(pingPongBecome, false);
	}
	
	@Override
	public void postStop() throws Exception {
		super.postStop();
		
		getContext().unbecome();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		// Nothing
		
		log.info("onReceive");
	}
	
	private class PingPongProcedure implements Procedure<Object> {

		@Override
		public void apply(Object message) throws Exception {
			log.info("PingPong state");
			
			if (message instanceof String) {
				String action = (String) message;
				log.info(action);
				count += 1;
				
				String nextAction = null;
				
				if (action.matches(PingPongUtils.PONG)) {
					nextAction = PingPongUtils.PING;
					
				} else if (action.matches(PingPongUtils.PING)) {
					nextAction = PingPongUtils.PONG;
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//
				}
				
				getSelf().tell(nextAction, getSelf());
			}
			
			if (count > 10) {
				getContext().stop(getSelf());
				getContext().system().shutdown();
			}
		}
		
	}
}