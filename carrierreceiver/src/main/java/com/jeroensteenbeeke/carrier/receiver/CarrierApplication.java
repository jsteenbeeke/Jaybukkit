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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class CarrierApplication extends Application {

	Set<Object> singletons = new HashSet<Object>();

	public CarrierApplication() {
		singletons.add(new FileServer());
		singletons.add(new TokenServer());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
