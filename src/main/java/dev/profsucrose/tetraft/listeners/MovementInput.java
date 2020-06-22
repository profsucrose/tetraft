package dev.profsucrose.tetraft.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.profsucrose.tetraft.Tetraft;
import dev.profsucrose.tetraft.commands.GameLoop;
import dev.profsucrose.tetraft.commands.Tetris;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class MovementInput implements Listener {

    @EventHandler
    public static void onPlayerOpenInventory(PlayerToggleSneakEvent e) {
        if (!Tetris.isGameGoing) return;

        e.setCancelled(true);
        Tetris.loop.holdPiece();
    }

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e) {
        if (!Tetris.isGameGoing) return;

        if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            Tetris.loop.rotateLeft();
        } else if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Tetris.loop.rotateRight();
        }

    }

    @EventHandler
    public static void onPlayerMovement(PlayerMoveEvent e) {

        if (!Tetris.isGameGoing) return;

        Location fromLocation = e.getFrom();
        Location toLocation = e.getTo();

        Player player = e.getPlayer();

        boolean shouldTeleport = true;

        double xDiff = Math.abs(toLocation.getX() - fromLocation.getX());
        double yDiff = toLocation.getY() - fromLocation.getY();
        double zDiff = Math.abs(toLocation.getZ() - fromLocation.getZ());

        if (xDiff > 0 || yDiff > 0 || zDiff > 0) {
            e.getPlayer().teleport(fromLocation.setDirection(toLocation.getDirection()));
        }


        if (yDiff > 0) {
            Tetris.loop.hardDrop();
            return;
        }

        if (zDiff > xDiff) {
            if (toLocation.getZ() - fromLocation.getZ() > 0) {
                Tetris.loop.moveDown();
            }
            return;
        }

        if (xDiff > zDiff) {
            if (toLocation.getX() - fromLocation.getX() > 0) {
                Tetris.loop.moveRight();
            } else {
                Tetris.loop.moveLeft();
            }
            return;
        }

    }

}
