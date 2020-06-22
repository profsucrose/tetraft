package dev.profsucrose.tetraft;

import dev.profsucrose.tetraft.commands.Tetris;
import dev.profsucrose.tetraft.listeners.MovementInput;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tetraft extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {

        System.out.println("Tetraft plugin enabled!");
        plugin = this;

        Utils.initPatterns();

        this.getCommand("tetris").setExecutor(new Tetris());
        getServer().getPluginManager().registerEvents(new MovementInput(), this);
    }

}

