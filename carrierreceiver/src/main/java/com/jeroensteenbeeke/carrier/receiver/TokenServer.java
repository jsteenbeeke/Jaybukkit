package com.jeroensteenbeeke.carrier.receiver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/auth")
public class TokenServer {
	@GET
	@Path("/{username}")
	public String getToken(@PathParam("username") String username) {
		return TokenRepository.INST.generateToken(username);
	}
}
