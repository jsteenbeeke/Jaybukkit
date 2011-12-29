/**
 * This file is part of Jaybukkit.
 *
 * Jaybukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaybukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jaybukkit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.carrier.receiver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/auth")
public class TokenServer {
	private static final Logger log = LoggerFactory
			.getLogger(TokenServer.class);

	@GET
	@Path("/{username}")
	@Produces("text/plain")
	public String getToken(@PathParam("username") String username) {
		String token = TokenRepository.INST.generateToken(username);

		log.info(String.format("Issued token %s for user %s", token, username));

		return token;
	}
}
