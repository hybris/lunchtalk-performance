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
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import rx.Observable;
import rx.functions.Func0;

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

		this.cache = CacheBuilder.newBuilder().maximumSize(10).build();
	}

	public <T> Observable<T> command(final String name, final Func0<T> func)
	{
		return new Command<>(name, func, "heartbeat", 1000, 10).observe();
	}

	public Client getClient()
	{
		return client;
	}

	public Cache<String, String> getCache()
	{
		return cache;
	}


	private static class Command<T> extends HystrixCommand<T>
	{

		private final Func0<T> func;

		Command(final String name, final Func0<T> func, final String group, final int timeoutMs, final int threadPoolCoreSize)
		{
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group)) //
					.andCommandKey(HystrixCommandKey.Factory.asKey(name)) //
					.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(group)) //
					.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter() //
							.withCoreSize(threadPoolCoreSize) //
							.withMaxQueueSize(Math.max(threadPoolCoreSize * 2 / 10, 8)) //
							.withQueueSizeRejectionThreshold(Math.max(threadPoolCoreSize * 2 / 10, 8))) //
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter() //
							.withExecutionIsolationThreadTimeoutInMilliseconds(timeoutMs)));

			this.func = func;
		}

		@Override
		protected T run()
		{
			return func.call();
		}
	}
}
