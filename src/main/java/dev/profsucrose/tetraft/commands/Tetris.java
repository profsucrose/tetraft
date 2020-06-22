package dev.profsucrose.tetraft.commands;

import dev.profsucrose.tetraft.Tetraft;
import dev.profsucrose.tetraft.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class Tetris implements CommandExecutor {

	public static int gameLoopID = -1;
	public static GameLoop loop;
	public static boolean isGameGoing = false;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (gameLoopID != -1) Bukkit.getScheduler().cancelTask(gameLoopID);

		isGameGoing = true;

		ItemStack i = new ItemStack(Material.WHITE_BANNER, 1);
		BannerMeta m = (BannerMeta)i.getItemMeta();

		m.setPatterns(Utils.numberBannerPatterns.get(0));
		i.setItemMeta(m);

		Player player = (Player) sender;

		player.getInventory().addItem(i);

		loop = new GameLoop(player);
		gameLoopID = Bukkit.getScheduler().scheduleSyncDelayedTask(
				Tetraft.plugin,
				loop,
				args.length > 0 ? Long.valueOf(args[0]) : 10L
		);

		return true;
	}

}
