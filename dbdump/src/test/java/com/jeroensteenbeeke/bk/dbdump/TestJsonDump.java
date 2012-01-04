package com.jeroensteenbeeke.bk.dbdump;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.text.json.JsonContext;
import com.avaje.ebean.text.json.JsonWriteOptions;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.jeroensteenbeeke.bk.dbdump.entities.Bar;
import com.jeroensteenbeeke.bk.dbdump.entities.Foo;

public class TestJsonDump {
	private static EbeanServer server;

	@BeforeClass
	public static void initDB() {
		Bar bar = new Bar();
		bar.setId("bar1");

		Foo foo = new Foo();
		foo.setId(1L);
		foo.setBar(bar);

		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUsername("sa");
		dsc.setPassword("");
		dsc.setDriver("org.h2.Driver");
		dsc.setUrl("jdbc:h2:mem:tests;DB_CLOSE_DELAY=-1");

		ServerConfig config = new ServerConfig();
		config.setName("h2");
		config.addClass(Foo.class);
		config.addClass(Bar.class);
		config.setDataSourceConfig(dsc);

		server = EbeanServerFactory.create(config);
		SpiEbeanServer serv = (SpiEbeanServer) server;
		DdlGenerator gen = serv.getDdlGenerator();

		gen.runScript(false, gen.generateCreateDdl());

		server.save(bar);
		server.save(foo);
	}

	@Test
	public void testJsonDump() {

		Foo foo = server.find(Foo.class).findUnique();

		JsonContext context = server.createJsonContext();
		JsonWriteOptions options = new JsonWriteOptions();
		options.setRootPathVisitor(new ForeignKeyVisitor(server));

		String res = context.toJsonString(foo, false, options);

		assertEquals("{\"id\":\"1\",\"bar\":\"bar1\"}", res);
	}
}
