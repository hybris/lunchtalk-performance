package com.hybris.lunchtalk;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Resource
@ManagedBean
@Singleton
public class HeartbeatService
{

	private final Client client;
	private final Cache<String, String> cache;

	public HeartbeatService()
	{
		this.client = ClientBuilder.newClient(new ClientConfig() //
				.property(ClientProperties.CONNECT_TIMEOUT, 1000) //
				.property(ClientProperties.READ_TIMEOUT, 1000) //
				.property(ClientProperties.FOLLOW_REDIRECTS, false) //
				.register(JacksonFeature.class).register(ObjectMapperResolver.class));

		this.cache = CacheBuilder.newBuilder().build();
	}

	public Client getClient()
	{
		return client;
	}

	public Cache<String, String> getCache()
	{
		return cache;
	}
}
