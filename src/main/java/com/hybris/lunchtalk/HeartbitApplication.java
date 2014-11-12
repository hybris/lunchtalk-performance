package com.hybris.lunchtalk;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class HeartbitApplication extends ResourceConfig
{
	public HeartbitApplication() {
		register(HeartbitResource.class);
		register(HeartbitService.class);
		register(JacksonFeature.class);
		register(JacksonJaxbJsonProvider.class);
		register(ObjectMapperResolver.class);
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(HeartbitService.class).to(HeartbitService.class);
			}
		});

		packages(true, "com.hybris.lunchtalk");

		property(ServerProperties.MONITORING_STATISTICS_ENABLED, true);
		property(ServerProperties.MONITORING_STATISTICS_REFRESH_INTERVAL, 2048); // ms
	}

}
