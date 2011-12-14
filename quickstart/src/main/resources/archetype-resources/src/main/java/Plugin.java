package ${package};

import java.util.logging.Logger;

import com.jeroensteenbeeke.bk.basics.JSPlugin;

public class Plugin extends JSPlugin {
	private Logger logger = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		logger.info("Enabled ${artifactId} plugin");

	}

	@Override
	public void onDisable() {

	}
}
