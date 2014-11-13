package com.hybris.lunchtalk;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("heartbeat")
public class HeartbeatResource
{
	final static String target = "http://repository-v2.dev.cf.hybris.com/internalstatus";

	@Inject
	public HeartbeatService service;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void getIt(@Suspended final AsyncResponse asyncResponse)
	{
		service.command("heartbeat-v0.docu-repo.rest", //
				() -> {
					final Response response = service.getClient().target(target).request().get();
					if (response.getStatus() != 200)
					{
						throw new ResourceUnavailableException("Status code " + response.getStatus());
					}
					return response;
				}) //
				.subscribe(response -> {
					service.getCache().put(response.toString(), response.getEntity().toString());
					asyncResponse.resume("{ \"status\" : \"ok\" }");
				}, throwable -> {
					service.getCache().put(throwable.toString(), throwable.getLocalizedMessage());
					asyncResponse.resume(Response.status(HTTP_INTERNAL_ERROR) //
							.entity("{ \"status\" : \"" + throwable.getMessage() + "\" }").build());
				});
	}
}
