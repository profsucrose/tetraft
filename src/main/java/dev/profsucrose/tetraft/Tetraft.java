package dev.profsucrose.tetraft;

import dev.profsucrose.tetraft.commands.Tetris;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tetraft extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("Tetraft plugin enabled!");
        this.getCommand("tetris").setExecutor(new Tetris());

        this.getCommand("_right").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                Tetris.loop.moveRight();
                return true;
            }
        });
        this.getCommand("_left").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                Tetris.loop.moveLeft();
                return true;
            }
        });
    }

}
