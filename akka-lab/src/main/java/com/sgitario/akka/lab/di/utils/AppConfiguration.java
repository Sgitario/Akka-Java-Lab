package com.sgitario.akka.lab.di.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;

/**
 * Spring specific configuration that is responsible for creating an
 * ActorSystem and configuring it as necessary. The actorSystem bean will be
 * a singleton.
 */
@Configuration
class AppConfiguration {
	@Bean
	public ActorSystem actorSystem() {
		return ActorSystem.create("dependencyInjection");
	}
}
