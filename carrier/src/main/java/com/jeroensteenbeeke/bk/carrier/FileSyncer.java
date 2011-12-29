package com.jeroensteenbeeke.bk.carrier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.bk.basics.util.HashUtil;

public class FileSyncer implements Runnable {
	private final Logger log = Logger.getLogger("Minecraft");

	private final String targetUrl;

	private final List<File> trackedFolders;

	private final String username;

	private final String password;

	private final JSONParser parser = new JSONParser();

	public FileSyncer(String targetUrl, String username, String password,
			List<String> trackedNames) {
		this.targetUrl = targetUrl;
		this.username = username;
		this.password = password;
		this.trackedFolders = new ArrayList<File>(trackedNames.size());
		for (String name : trackedNames) {
			File f = new File(name);
			if (f.exists()) {
				trackedFolders.add(f);
			}
		}
	}

	@Override
	public void run() {
		try {
			String token = getString(newClient(), createTokenRequest());
			Set<String> remote = new HashSet<String>(getList(newClient(),
					createFileListRequest(token)));
			Map<String, File> local = new HashMap<String, File>();

			for (File folder : trackedFolders) {
				File[] filesInFolder = folder.listFiles();
				for (File f : filesInFolder) {
					local.put(f.getName(), f);
				}
			}

			// Delete remote but not local
			for (String fn : remote) {
				if (!local.containsKey(fn)) {
					token = getString(newClient(), createTokenRequest());
					int response = perform(newClient(),
							createDeleteFileRequest(token, fn));
					log.info(String.format("Delete remote file %s result %d",
							fn, response));
				}
			}

			// Upload local but not remote
			for (Entry<String, File> e : local.entrySet()) {
				if (!remote.contains(e.getKey())) {
					token = getString(newClient(), createTokenRequest());
					int response = perform(newClient(),
							createUploadFileRequest(token, e.getValue()));
					log.info(String.format("Upload file %s result %d",
							e.getKey(), response));
				}
			}

		} catch (IOException ioe) {
			log.severe("Failed to communicate with carrier receiver: "
					+ ioe.getMessage());
		} catch (ParseException pe) {
			log.severe("Failed to parse response: " + pe.getMessage());
		}

	}

	private HttpClient newClient() {
		return new DefaultHttpClient();
	}

	private HttpUriRequest createUploadFileRequest(String token, File value) {
		HttpPost request = new HttpPost(withQueryParams(
				constructUri("files", value.getName()), "token", token,
				"signature", sign(token, value.getName(), password)));

		FileEntity entity = new FileEntity(value, "application/octet-stream");
		request.setEntity(entity);

		return request;
	}

	public HttpUriRequest createTokenRequest() {
		return new HttpGet(constructUri("auth", username));
	}

	public HttpUriRequest createFileListRequest(String token) {
		return new HttpGet(withQueryParams(constructUri("files"), "token",
				token, "signature", sign(token, "", password)));
	}

	public HttpUriRequest createDeleteFileRequest(String token, String file) {
		return new HttpDelete(withQueryParams(constructUri("files", file),
				"token", token, "signature", sign(token, file, password)));
	}

	private String withQueryParams(String base, String... parts) {
		try {
			if (parts.length % 2 == 0) {
				StringBuilder uri = new StringBuilder();
				uri.append(base);
				uri.append("?");

				for (int i = 0; i < parts.length; i += 2) {
					if (i > 0) {
						uri.append("&");
					}

					uri.append(URLEncoder.encode(parts[i], "UTF-8"));
					uri.append("=");
					uri.append(URLEncoder.encode(parts[i + 1], "UTF-8"));
				}

				return uri.toString();
			}
		} catch (UnsupportedEncodingException uee) {
			log.severe("Unsupported encoding UTF-8");
		}

		return base;
	}

	private String constructUri(String... parts) {
		StringBuilder uri = new StringBuilder();
		uri.append(targetUrl);

		for (String part : parts) {
			if (!uri.toString().endsWith("/")) {
				uri.append("/");
			}

			uri.append(part);
		}

		return uri.toString();
	}

	private String sign(String token, String subject, String password) {
		StringBuilder input = new StringBuilder();
		input.append(token);
		input.append('!');
		input.append(subject);
		input.append('!');
		input.append(password);

		return HashUtil.sha1Hash(input.toString());
	}

	public int perform(HttpClient client, HttpUriRequest request)
			throws IOException {
		HttpResponse response = client.execute(request);

		return response.getStatusLine().getStatusCode();
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(HttpClient client, HttpUriRequest request)
			throws IOException, ParseException {
		String res = getString(client, request);

		Object o = parser.parse(res);
		if (o instanceof JSONArray) {
			return (JSONArray) o;
		} else if (o instanceof JSONValue) {
			return Lists.newArrayList(((JSONValue) o).toString());
		}

		return Lists.newArrayList();
	}

	public String getString(HttpClient client, HttpUriRequest request)
			throws IOException {
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		StringBuilder r = new StringBuilder();
		int b;
		while ((b = is.read()) != -1) {
			r.append((char) b);
		}

		return r.toString();
	}
}
