package dev.profsucrose.tetraft.commands;

import dev.profsucrose.tetraft.models.Tetromino;
import dev.profsucrose.tetraft.models.TetrominoType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class GameLoop implements Runnable {

    private enum Mode { PIECE_FALLING, NEW_PIECE }
    private Mode mode = Mode.NEW_PIECE;
    private static Random random = new Random();
    private Board board;
    private boolean didJustCreatePiece;
    private Player player;

    Tetromino currentPiece;
    TetrominoType[] bag;
    int bagIndex;

    static Tetromino getRandomTetromino() {
        return new Tetromino(TetrominoType.values()[random.nextInt(TetrominoType.values().length)]);
    }

    static TetrominoType[] genBag() {
        TetrominoType[] bag = new TetrominoType[7];
        int startingIndex = random.nextInt(7);
        for (int i = 0; i < 7; i++) bag[(i + startingIndex) % 7] = TetrominoType.values()[i];
        return bag;
    }

    public GameLoop(Player player) {
        this.bag = genBag();
        this.bagIndex = 0;
        this.currentPiece = new Tetromino(bag[bagIndex]);
        this.board = new Board();
        this.player = player;
    }

    public void moveRight() {
        board.movePieceRight(currentPiece);
    }

    public void moveLeft() {
        board.movePieceLeft(currentPiece);
    }

    @Override
    public void run() {
        System.out.println(
                String.format("Piece type: %s Piece Y: %d", currentPiece.type.name(), currentPiece.y)
        );

        currentPiece.y -= 1;

        boolean collision = false;
        for (int i = 0; i < 4; i++) {
            int coordX = currentPiece.x + currentPiece.coords[i][0];
            int coordY = currentPiece.y + currentPiece.coords[i][1];
            if (coordY < 0
                    || board.get(coordX, coordY) != TetrominoType.Empty) {
                collision = true;
                break;
            }
        }

        if (collision) {
            if (didJustCreatePiece) {
                Bukkit.getScheduler().cancelTask(Tetris.gameLoopID);
                player.sendMessage(ChatColor.RED + "Game over!");
                return;
            }

            System.out.println("Collision!");
            board.setPiece(currentPiece);

            if (bagIndex == bag.length) {
                bag = genBag();
                bagIndex = 0;
            }
            bagIndex += 1;
            currentPiece = new Tetromino(bag[bagIndex]);
            didJustCreatePiece = true;
            System.out.println(String.format("Created new piece : %s", currentPiece.type.name()));
        } else {
            didJustCreatePiece = false;

            currentPiece.y += 1;
            board.movePieceDown(currentPiece);
        }

    }

}
