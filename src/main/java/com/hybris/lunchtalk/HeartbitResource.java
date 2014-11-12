package com.hybris.lunchtalk;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("heartbit")
public class HeartbitResource
{
	final static String target = "http://repository-v2.dev.cf.hybris.com/internalstatus";

	@Inject
	public HeartbitService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void getIt(@Suspended final AsyncResponse asyncResponse)
	{

		service.getClient().target(target).request().async().get(new InvocationCallback<Response>()
		{
			@Override
			public void completed(Response response)
			{
				service.getCache().put(response.toString(), response.getEntity().toString());
				asyncResponse.resume("{ \"status\" : \"ok\" }");
			}

			@Override
			public void failed(Throwable throwable)
			{
				service.getCache().put(throwable.toString(), throwable.getLocalizedMessage());
				asyncResponse.resume(Response.status(HTTP_INTERNAL_ERROR).build());
			}
		});
	}
}
