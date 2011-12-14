package com.jeroensteenbeeke.carrier.receiver;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

@Path("/files")
public class FileServer {
	@GET
	@Path("/")
	public List<String> getFiles(@QueryParam("token") String token,
			@QueryParam("signature") String signature) {
		if (TokenRepository.INST.verifySignature(token, "", signature)) {
			// TODO actual list
			return Collections.emptyList();
		}

		throw new WebApplicationException(Status.FORBIDDEN);
	}

	@POST
	@Path("/{name}")
	public void uploadFile(@QueryParam("token") String token,
			@QueryParam("signature") String signature,
			@PathParam("name") String name, @FormParam("file") File file) {
		if (TokenRepository.INST.verifySignature(token, name, signature)) {
			// TODO actual action
			return;
		}

		throw new WebApplicationException(Status.FORBIDDEN);
	}

	@DELETE
	@Path("/{name}")
	public void deleteFile(@QueryParam("token") String token,
			@QueryParam("signature") String signature,
			@PathParam("name") String name, @FormParam("file") File file) {
		if (TokenRepository.INST.verifySignature(token, name, signature)) {
			// TODO actual action
			return;
		}

		throw new WebApplicationException(Status.FORBIDDEN);
	}
}
