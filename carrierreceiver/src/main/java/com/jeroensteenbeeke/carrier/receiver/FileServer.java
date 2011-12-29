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
import java.io.FileOutputStream;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/files")
public class FileServer {
	private Logger log = LoggerFactory.getLogger(FileServer.class);

	private File dataFolder;

	public FileServer() {
		String dataFolderName = System.getProperty("data.folder");

		if (dataFolderName == null) {
			log.error("NO DATA FOLDER INDICATED! ALL WEB SERVICE CALLS WILL FAIL!");
			log.error("Please start this server with -Ddata.folder=/location/of/data/folder");

		} else {

			dataFolder = new File(dataFolderName);

			if (dataFolder.exists()) {
				log.error("Data folder does not exist: " + dataFolderName);
			} else if (!dataFolder.isDirectory()) {
				log.error("Data folder is not a directory: " + dataFolderName);
			}
		}
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public String[] getFiles(@QueryParam("token") String token,
			@QueryParam("signature") String signature) {
		if (TokenRepository.INST.verifySignature(token, "", signature)) {
			log.info(String.format("GET /files/ signed %s with token %s",
					signature, token));

			return dataFolder.list();
		}

		log.warn(String.format(
				"Auth failed for GET /files/ signed %s with token %s",
				signature, token));

		throw new WebApplicationException(Status.FORBIDDEN);
	}

	@POST
	@Path("/{name}")
	@Consumes("application/octet-stream")
	@Produces("text/plain")
	public String uploadFile(@QueryParam("token") String token,
			@QueryParam("signature") String signature,
			@PathParam("name") String name, byte[] data) {
		if (TokenRepository.INST.verifySignature(token, name, signature)) {

			File target = new File(dataFolder, name);

			try {
				FileOutputStream out = new FileOutputStream(target);

				for (byte b : data) {
					out.write(b);
				}

				out.close();

				log.info(String.format(
						"POST /files/%s signed %s with token %s", name,
						signature, token));
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
			}

			return String.format("File %s created", name);
		}

		log.warn(String.format(
				"Auth failed for POST /files/%s signed %s with token %s", name,
				signature, token));

		throw new WebApplicationException(Status.FORBIDDEN);
	}

	@DELETE
	@Path("/{name}")
	@Produces("text/plain")
	public String deleteFile(@QueryParam("token") String token,
			@QueryParam("signature") String signature,
			@PathParam("name") String name) {
		if (TokenRepository.INST.verifySignature(token, name, signature)) {
			File target = new File(dataFolder, name);

			if (target.exists()) {
				target.delete();

				log.info(String.format(
						"DELETE /files/%s signed %s with token %s", name,
						signature, token));
			} else {
				log.warn(String
						.format("File not found on DELETE /files/%s signed %s with token %s",
								name, signature, token));

				throw new WebApplicationException(Status.NOT_FOUND);
			}

			return String.format("File %s deleted", name);
		}

		log.warn(String.format(
				"Auth failed for DELETE /files/%s signed %s with token %s",
				name, signature, token));

		throw new WebApplicationException(Status.FORBIDDEN);
	}
}
