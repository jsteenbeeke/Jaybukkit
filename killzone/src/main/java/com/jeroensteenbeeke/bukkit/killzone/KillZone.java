package com.jeroensteenbeeke.bukkit.killzone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;
import com.jeroensteenbeeke.bukkit.killzone.handlers.KillZoneHandler;

public class KillZone extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	private ZoneTracker tracker;

	public KillZone() {

	}

	@Override
	public void onEnable() {
		logger.info("Enabled killzone plugin");

		File dataFolder = getDataFolder();

		tracker = new ZoneTracker();

		if (dataFolder.exists()) {
			initKillzones(dataFolder);

		}

		addListener(new KillZoneHandler(tracker));
	}

	private void initKillzones(File dataFolder) {
		logger.info("Scanning new killzone data");

		File[] killfiles = dataFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".killzone");
			}
		});

		for (File kf : killfiles) {
			logger.info("Parsing " + kf.getName());
			processFile(kf);
		}
	}

	private void processFile(File kf) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(kf));
			String next = null;
			int i = 1;
			while ((next = br.readLine()) != null) {
				parseLine(i++, next);
			}

		} catch (IOException ioe) {
			logger.severe("Could not parse " + kf.getName() + ": "
					+ ioe.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.fine("Error closing stream: " + e.getMessage());
				}
			}
		}
	}

	private void parseLine(int number, String line) {
		final String[] fields = line.split("\\s");

		if (fields.length == 7) {
			try {
				final String world = fields[0];
				final int x1 = Integer.parseInt(fields[1]);
				final int y1 = Integer.parseInt(fields[2]);
				final int z1 = Integer.parseInt(fields[3]);
				final int x2 = Integer.parseInt(fields[4]);
				final int y2 = Integer.parseInt(fields[5]);
				final int z2 = Integer.parseInt(fields[6]);

				logger.info(String.format(
						"Adding zone (%d, %d, %d) to (%d, %d, %d)", x1, y1, z1,
						x2, y2, z2));

				final int lowerX = Math.min(x1, x2);
				final int upperX = Math.max(x1, x2);

				final int lowerY = Math.min(y1, y2);
				final int upperY = Math.max(y1, y2);

				final int lowerZ = Math.min(z1, z2);
				final int upperZ = Math.max(z1, z2);

				int i = 0;

				for (int x = lowerX; x <= upperX; x++) {
					for (int y = lowerY; y <= upperY; y++) {
						for (int z = lowerZ; z <= upperZ; z++) {
							tracker.registerBlock(world, x, y, z);
							i++;
						}
					}
				}

				logger.info(String.format("Killzone contains %d blocks", i));

			} catch (NumberFormatException nfe) {
				logger.warning(String.format("Skipped invalid line %d: %s",
						number, line));
				logger.warning("Expected format: worldname x1 y1 z1 x2 y2 z2");
			}

		} else {
			logger.warning(String.format("Skipped invalid line %d: %s", number,
					line));
			logger.warning("Expected format: worldname x1 y1 z1 x2 y2 z2");
		}

	}

	@Override
	public void onDisable() {

	}
}
