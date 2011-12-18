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
package com.jeroensteenbeeke.bk.ville.commands;

import com.jeroensteenbeeke.bk.basics.commands.PlayerAwareCommandHandler;
import com.jeroensteenbeeke.bk.ville.Ville;
import com.jeroensteenbeeke.bk.ville.VilleLocations;

public abstract class AbstractVilleCommandHandler extends
		PlayerAwareCommandHandler {
	private Ville ville;

	public AbstractVilleCommandHandler(Ville ville, PermissionPolicy policy,
			String... requiredPermissions) {
		super(ville.getServer(), policy, requiredPermissions);
		this.ville = ville;
	}

	public AbstractVilleCommandHandler(Ville ville,
			String... requiredPermissions) {
		super(ville.getServer(), requiredPermissions);
		this.ville = ville;
	}

	protected final Ville getVille() {
		return ville;
	}

	protected final VilleLocations getLocationsHandle() {
		return ville.getLocations();
	}

}
