package com.jeroensteenbeeke.bk.jayconomy;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.bukkit.Material;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.SqlQuery;
import com.jeroensteenbeeke.bk.jayconomy.entities.JayconomyMaterial;

public class Worth {
	private static final Logger log = Logger.getLogger("Minecraft");

	public static void initializeWorth(EbeanServer server) {
		initWorth(server, Material.DIRT, 64, 64);
		initWorth(server, Material.COBBLESTONE, 64, 64);
		initWorth(server, Material.WOOD, 64, 64);
		initWorth(server, Material.SAND, 64, 64);
		initWorth(server, Material.GRAVEL, 64, 64);

		initContrabandWorth(server, Material.SULPHUR, 19, 1);
		initContrabandWorth(server, Material.TNT, 150, 1);

	}

	private static void initWorth(EbeanServer server, Material material,
			int value, int stacksize) {
		initWorth(server, material, new BigDecimal(value), true, stacksize);
	}

	private static void initContrabandWorth(EbeanServer server,
			Material material, int value, int stacksize) {
		initWorth(server, material, new BigDecimal(value), false, stacksize);
	}

	private static void initWorth(EbeanServer server, Material material,
			BigDecimal value, boolean resellable, int stacksize) {
		if (!hasEntry(server, material)) {

			JayconomyMaterial m = createMaterialData(material, value,
					resellable, stacksize);

			log.info("Set value of " + material.name() + " to "
					+ value.toString() + " with" + (resellable ? "" : "out")
					+ " resell option");

			server.save(m);
		}

	}

	private static boolean hasEntry(EbeanServer server, Material material) {
		SqlQuery q = server
				.createSqlQuery(
						"select count(*) as c from jayconomy_material where item_id=:id")
				.setParameter("id", material.getId());

		Integer count = q.findUnique().getInteger("c");

		return count != null && count > 0;
	}

	private static JayconomyMaterial createMaterialData(Material material,
			BigDecimal value, boolean resellable, int stacksize) {
		JayconomyMaterial m = new JayconomyMaterial();
		m.setItemId(material.getId());
		m.setSellable(resellable);
		m.setWorth(value);
		m.setStackSize(stacksize);

		return m;
	}
}
