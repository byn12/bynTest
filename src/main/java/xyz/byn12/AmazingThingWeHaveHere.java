package xyz.byn12;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * PluginTemplate is a simple template you can use every time you make
 * a new plugin. This will save you time because you no longer have to
 * recreate the same skeleton and features each time.
 *
 * It uses Foundation for fast and efficient development process.
 */
public final class AmazingThingWeHaveHere extends SimplePlugin {
	/**
	* Automatically perform login ONCE when the plugin starts.
	*/
	@Override
	protected void onPluginStart() {

	}

	/**
	 * Automatically perform login when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {

		// You can check for necessary plugins and disable loading if they are missing
		//Valid.checkBoolean(HookManager.isVaultLoaded(), "You need to install Vault so that we can work with packets, offline player data, prefixes and groups.");

		// Uncomment to load variables
		// Variable.loadVariables();

		//
		// Add your own plugin parts to load automatically here
		// Please see @AutoRegister for parts you do not have to register manually
		//
	}

	@Override
	protected void onPluginPreReload() {

		// Close your database here if you use one
		//YourDatabase.getInstance().close();
	}

	/* ------------------------------------------------------------------------------- */
	/* Events */
	/* ------------------------------------------------------------------------------- */

	/**
	 * An example event that checks if the right clicked entity is a cow, and makes an explosion.
	 * You can write your events to your main class without having to register a listener.
	 *
	 * @param event
	 */
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (event.getHand() == EquipmentSlot.HAND && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Bukkit.broadcastMessage(setBlockType(event.getClickedBlock(), player));

		}

		// event.getRightClicked().getWorld().createExplosion(event.getRightClicked().getLocation(), 100);

	}

	@EventHandler
	public void onMine(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		ItemStack[] drops = block.getDrops().toArray(new ItemStack[0]);
		try {
			block.getWorld().dropItem(block.getLocation().add(0.5, 1, 0.5), drops[0]).setVelocity(new Vector(0, 0, 0));
		} catch (Exception e) {
			System.out.println("There was an error: " + e);
		}
		event.setCancelled(true);
		iWasJustMined(block);
	}

	void iWasJustMined(Block mined) {
		Material oldBlock = mined.getType();
		mined.setType(Material.BEDROCK);
		Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
			if (!oldBlock.equals(Material.BEDROCK))
				mined.setType(oldBlock);
			else
				System.out.println("Tried breaking bedrock.");
		}, 60);
	}


	String setBlockType(Block block, Player player) {
		try {
			Material material = player.getInventory().getItemInMainHand().getType();
			String oldBlock = block.getType().name();
			block.setType(material);

			return "Block was set from " + WordUtils.capitalizeFully(oldBlock.replace("_", " ")) + " to " + WordUtils.capitalizeFully(material.name().replace("_", " "));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* ------------------------------------------------------------------------------- */
	/* Static */
	/* ------------------------------------------------------------------------------- */

	/**
	 * Return the instance of this plugin, which simply refers to a static
	 * field already created for you in SimplePlugin but casts it to your
	 * specific plugin instance for your convenience.
	 *
	 * @return
	 */
	public static AmazingThingWeHaveHere getInstance() {
		return (AmazingThingWeHaveHere) SimplePlugin.getInstance();
	}

}
