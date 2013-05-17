package com.jeroensteenbeeke.bukkit;

import java.util.List;

import org.junit.Test;

import com.jeroensteenbeeke.bukkit.EnderQuestCommand.Notification;

public class PluginTest {
	@Test
	public void testNotificationMoments() {
		List<Notification> notificationTimes = EnderQuestCommand
				.getNotificationTimes(120);

		for (Notification notif : notificationTimes) {
			System.out.printf("At %d say %d remaining", notif.getMoment(),
					notif.getMinutes());
			System.out.println();
		}
	}
}
