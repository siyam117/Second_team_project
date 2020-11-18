/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.item.consumption;

import games.stendhal.server.entity.item.ConsumableItem;
import games.stendhal.server.entity.item.StatusHealer;

public final class FeederFactory {
	private static Stuffer stuffer = new Stuffer();
	private static Enchanter enchanter = new Enchanter();
	private static Immunizer immunizer = new Immunizer();
	private static Poisoner poisoner = new Poisoner();
	private static Eater eater = new Eater();
	private static PoisonRecover poison_recover = new PoisonRecover();
	private static ConfuseRecover confuse_recover = new ConfuseRecover();
	private static ShockRecover shock_recover = new ShockRecover();

	public static Feeder get(final ConsumableItem item) {
		if (item.getItemSubclass().equals("poison_healer")) {
			return poison_recover;
		}
		
		if (item.getItemSubclass().equals("shock_healer")) {
			return shock_recover;
		}
		
		if (item.getItemSubclass().equals("confuse_healer")) {
			return confuse_recover;
		}

		if (item instanceof StatusHealer) {
			return immunizer;
		}

		if (item.getName().contains("potion")) {
			return stuffer;
		}

		if(item.getName().contains("mana")) {
			return enchanter;
		}

		if (item.getRegen() == 0) {
			return immunizer;
		} else if (item.getRegen() < 0) {
			return poisoner;
		} else {
			return eater;
		}

	}

}
