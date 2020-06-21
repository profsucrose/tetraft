package dev.profsucrose.tetraft.commands;

import dev.profsucrose.tetraft.Tetraft;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tetris implements CommandExecutor {

	public static int gameLoopID = -1;
	public static GameLoop loop;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (gameLoopID != -1) Bukkit.getScheduler().cancelTask(gameLoopID);

		TextComponent message = new TextComponent();
		TextComponent leftButton = new TextComponent("[Left]");
		TextComponent rightButton = new TextComponent("[Right]");

		rightButton.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/_right" ) );
		leftButton.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/_left" ) );

		Player player = (Player) sender;
		message.addExtra("" + ChatColor.BOLD + ChatColor.GREEN);
		message.addExtra(leftButton);
		message.addExtra(" ");
		message.addExtra(rightButton);
		player.spigot().sendMessage(message);

		loop = new GameLoop(player);
		gameLoopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				Tetraft.plugin,
				loop,
				0L,
				args.length > 0 ? Long.valueOf(args[0]) : 10L
		);

		return true;
	}

}
