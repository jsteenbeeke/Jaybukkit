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
