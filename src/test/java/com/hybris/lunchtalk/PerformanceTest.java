package com.hybris.lunchtalk;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Ignore;
import org.junit.Test;
import org.marekasf.troughput.ThroughputRunner;

import rx.Observable;

@Ignore("manual stress test")
public class PerformanceTest
{
	@Test
	public void restEndpointPerformance() throws InterruptedException
	{
		final Invocation.Builder builder = ClientBuilder.newClient(new ClientConfig() //
				.property(ClientProperties.CONNECT_TIMEOUT, 2500) //
				.property(ClientProperties.READ_TIMEOUT, 2500) //
				.property(ClientProperties.ASYNC_THREADPOOL_SIZE, 2500) //
				.property(ClientProperties.FOLLOW_REDIRECTS, false) //
				.register(JacksonFeature.class).register(ObjectMapperResolver.class)) //
				.target("http://localhost:8080/rest/heartbeat").request();

		ThroughputRunner.Builder.create().threads(2).testTimeInSeconds(5 * 60).action(() -> {
			final Response response = builder.get();
			try
			{
				if (response.getStatus() != 200)
				{
					return Observable.error(new IllegalArgumentException("Response " + response.getStatus()));
				}
				return Observable.just(true);
			}
			finally
			{
				response.close();
			}
		}).run();
	}

}
