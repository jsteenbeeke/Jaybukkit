package com.jeroensteenbeeke.bk.ville.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jeroensteenbeeke.bk.basics.commands.CommandMatcher;
import com.jeroensteenbeeke.bk.basics.commands.ParameterIntegrityChecker;
import com.jeroensteenbeeke.bk.basics.util.Messages;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;

public class VilleFreeBuildCommandHandler extends AbstractVilleCommandHandler {
	public VilleFreeBuildCommandHandler(Ville ville) {
		super(ville, Ville.PERMISSION_USE);
	}

	@Override
	public CommandMatcher getMatcher() {
		return ifNameIs("ville").andArgIs(0, "free").andArgIs(1, "build")
				.itMatches();
	}

	@Override
	public ParameterIntegrityChecker getParameterChecker() {
		return ifArgCountIs(2).andArgumentEquals(0, "free")
				.andArgumentEquals(1, "build").itIsProper();
	}

	@Override
	public void onAuthorizedAndPlayerFound(Player player, Command command,
			String label, String[] args) {
		List<VillageLocation> locs = getVille().getDatabase()
				.find(VillageLocation.class).where().eq("entryLevel", true)
				.eq("world", player.getWorld().getName()).findList();

		if (locs.isEmpty()) {
			Messages.send(player,
					"&cThere are no free build areas in this world");
		} else {
			SortedMap<Integer, VillageLocation> closest = determineClosest(
					player, locs, 5);
			Messages.send(player, "&fClosest free build areas:");
			for (Entry<Integer, VillageLocation> e : closest.entrySet()) {
				Integer dist = e.getKey();
				VillageLocation vl = e.getValue();

				Messages.send(
						player,
						String.format(" - &e%s&f at &e%d&f blocks distance",
								vl.getName(), dist));

			}

		}

	}

	private SortedMap<Integer, VillageLocation> determineClosest(Player player,
			List<VillageLocation> locs, int max) {
		SortedMap<Integer, VillageLocation> closest = new TreeMap<Integer, VillageLocation>();
		Location pl = player.getLocation();

		for (VillageLocation loc : locs) {
			Integer distance = getLocationsHandle().getDistance(loc, pl);
			closest.put(distance, loc);
		}

		List<Integer> keys = new ArrayList<Integer>(max);

		if (closest.size() > max) {
			Iterator<Integer> keyIt = closest.keySet().iterator();
			while (keys.size() < max && keyIt.hasNext()) {
				keys.add(keyIt.next());
			}
		} else {
			keys.addAll(closest.keySet());
		}

		return closest;
	}

}
