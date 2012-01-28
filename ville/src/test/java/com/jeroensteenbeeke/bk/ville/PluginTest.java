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
package com.jeroensteenbeeke.bk.ville;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.bk.ville.entities.VillageLocation;
import com.jeroensteenbeeke.bk.ville.listeners.BuildPermissionListener;

@RunWith(PowerMockRunner.class)
public class PluginTest {
	private Ville ville;

	private VilleLocations locs;

	private BlockListener listener;

	@Before
	public void createMocks() {
		ville = mock(Ville.class);
		locs = mock(VilleLocations.class);

		when(ville.getLocations()).thenReturn(locs);
		when(locs.hasBuilderPermission(any(Player.class), any(Location.class)))
				.thenCallRealMethod();
		when(locs.isApprovedBuilder(any(Player.class))).thenCallRealMethod();

		listener = new BuildPermissionListener(ville);

	}

	@After
	public void cleanupMocks() {
		ville = null;
	}

	@Test
	public void testApprovedPlayerInOwnedRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();
		builders.put(vl, "JeroenFM");

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(Sets.newHashSet("JeroenFM"));

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());

	}

	@Test
	public void testApprovedPlayerInBuilderRightsRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();
		builders.put(vl, "Pienterekaak");

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(
				Sets.newHashSet("Pienterekaak"));

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}

	@Test
	public void testApprovedPlayerInUnownedRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(
				Sets.newHashSet("Pienterekaak"));

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertTrue(event.isCancelled());
	}

	@Test
	public void testApprovedPlayerOutsideRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		when(locs.getApprovedPlayers()).thenReturn(
				Sets.newHashSet("Pienterekaak"));

		when(locs.getJurisdiction(loc)).thenReturn(null);
		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}

	@Test
	public void testPremiumMemberInOwnedRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(true);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());

	}

	@Test
	public void testPremiumMemberInBuilderRightsRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(true);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();
		builders.put(vl, "Pienterekaak");

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}

	@Test
	public void testPremiumMemberInUnownedRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(true);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertTrue(event.isCancelled());
	}

	@Test
	public void testPremiumMemberOutsideRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(true);

		when(locs.getApprovedPlayers()).thenReturn(
				Sets.newHashSet("Pienterekaak"));

		when(locs.getJurisdiction(loc)).thenReturn(null);
		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}

	@Test
	public void testUnapprovedPlayerInOwnedRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(false);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("JeroenFM");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(new HashSet<String>());

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertTrue(event.isCancelled());
	}

	@Test
	public void testUnapprovedPlayerInBuilderRightsRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(false);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("Pienterekaak");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();
		builders.put(vl, "JeroenFM");

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(new HashSet<String>());

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertTrue(event.isCancelled());
	}

	@Test
	public void testUnapprovedInUnownedRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(false);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("Pienterekaak");
		when(vl.isEntryLevel()).thenReturn(false);
		when(vl.isRestricted()).thenReturn(true);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(new HashSet<String>());

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertTrue(event.isCancelled());
	}

	@Test
	public void testUnapprovedPlayerOutsideRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("Pienterekaak");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(false);

		when(locs.getApprovedPlayers()).thenReturn(new HashSet<String>());

		when(locs.getJurisdiction(loc)).thenReturn(null);
		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertTrue(event.isCancelled());
	}

	@Test
	public void testPremiumInFreeBuildRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(true);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("Pienterekaak");
		when(vl.isEntryLevel()).thenReturn(true);
		when(vl.isRestricted()).thenReturn(false);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(new HashSet<String>());

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}

	@Test
	public void testApprovedInFreeBuildRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(false);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("Pienterekaak");
		when(vl.isEntryLevel()).thenReturn(true);
		when(vl.isRestricted()).thenReturn(false);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(Sets.newHashSet("JeroenFM"));

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}

	@Test
	public void testUnapprovedInFreeBuildRegion() {
		Location loc = mock(Location.class);
		Player player = mock(Player.class);
		when(player.getLocation()).thenReturn(loc);
		when(player.getName()).thenReturn("JeroenFM");
		when(player.hasPermission(Ville.PERMISSION_PREMIUM)).thenReturn(false);

		VillageLocation vl = mock(VillageLocation.class);
		when(vl.getOwner()).thenReturn("Pienterekaak");
		when(vl.isEntryLevel()).thenReturn(true);
		when(vl.isRestricted()).thenReturn(false);

		Multimap<VillageLocation, String> builders = LinkedListMultimap
				.create();

		when(locs.getBuilders()).thenReturn(builders);
		when(locs.getJurisdiction(loc)).thenReturn(vl);
		when(locs.getApprovedPlayers()).thenReturn(new HashSet<String>());

		Block block = mock(Block.class);
		when(block.getLocation()).thenReturn(loc);

		BlockBreakEvent event = new BlockBreakEvent(block, player);

		listener.onBlockBreak(event);

		assertFalse(event.isCancelled());
	}
}
