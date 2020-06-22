package dev.profsucrose.tetraft.commands;

import dev.profsucrose.tetraft.Tetraft;
import dev.profsucrose.tetraft.Utils;
import dev.profsucrose.tetraft.models.Tetromino;
import dev.profsucrose.tetraft.models.TetrominoType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class GameLoop implements Runnable {

    private static int[] firstBagPieceDisplayTopLeftCorner      = {   8, 24, -12 };
    private static int[] firstBagPieceDisplayBottomRightCorner  = {  12,  9, -12 };
    private static int[] holdPieceDisplayTopLeftCorner          = { -13, 25, -12 };
    private static int[] holdPieceDisplayBottomRightCorner      = {  -9, 21, -12 };
    private static int[] numberBannerStartLoc                   = {   4,  5, -15 };
    private static int[] scoreStartLoc                          = {  -8, 15, -11 };
    private static int[] lineCountStartLoc                      = {  -8,  9, -11 };
    private static int[] levelLoc                               = {  -8,  6, -11 };
    private static Random random = new Random();

    private Board board;
    private boolean didJustCreatePiece;
    private Player player;
    private Tetromino currentPiece;
    private TetrominoType holdPieceType = TetrominoType.Empty;
    private TetrominoType[] bag;
    private TetrominoType[] bag2;
    private boolean justHoldPiece;
    private boolean hasHoldPiece;
    private int bagIndex;
    private int linesClearedCount;
    private int level;
    private int score;

    private int calculateScore(int lines) {
        int base = 0;
        if (lines == 1) base = 40;
        if (lines == 2) base = 100;
        if (lines == 3) base = 300;
        if (lines == 4) base = 1200;
        return base * (int)Math.pow(2, level);
    }

    static private void writeNumber(int number, int x, int y, int z) {
        String numberString = String.valueOf(number);
        for (int i = 0; i < numberString.length(); i++) {
            int digit = Character.getNumericValue(numberString.charAt(numberString.length() - i - 1));
            System.out.println(String.format(
                    "clone %1$d %2$d %3$d %1$d %2$d %3$d %4$d %5$d %6$d",
                    numberBannerStartLoc[0] - digit,
                    numberBannerStartLoc[1],
                    numberBannerStartLoc[2],
                    x - i,
                    y,
                    z
            ));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format(
                    "clone %1$d %2$d %3$d %1$d %2$d %3$d %4$d %5$d %6$d",
                    numberBannerStartLoc[0] - digit,
                    numberBannerStartLoc[1],
                    numberBannerStartLoc[2],
                    x - i,
                    y,
                    z
            ));
        }
    }

    static private TetrominoType[] genBag() {
        TetrominoType[] bag = new TetrominoType[7];
        int startingIndex = random.nextInt(7);
        for (int i = 0; i < 7; i++) bag[(i + startingIndex) % 7] = TetrominoType.values()[i];
        return bag;
    }

    private void placeBagPieceDisplayBlocks() {

        Utils.fillArea(
            "world",
            firstBagPieceDisplayTopLeftCorner[0],
            firstBagPieceDisplayBottomRightCorner[1],
            firstBagPieceDisplayTopLeftCorner[2],
            firstBagPieceDisplayBottomRightCorner[0],
            firstBagPieceDisplayTopLeftCorner[1],
            firstBagPieceDisplayBottomRightCorner[2],
            Material.AIR
        );

        for (int i = 0; i < 4; i++) {
            TetrominoType type = (i + bagIndex) > 6
                            ? bag2[(i + bagIndex) % 7]
                            : bag[i + bagIndex];
            Tetromino piece = new Tetromino(type);

            if (piece.type == TetrominoType.I) piece.rotateRight();

            for (int j = 0; j < 4; j++) {
                Utils.placeTetrominoTypeAsBlock(
                    "world",
                    firstBagPieceDisplayTopLeftCorner[0] + piece.coords[j][0] + 2,
                    firstBagPieceDisplayTopLeftCorner[1] - i*4 + piece.coords[j][1] - 2,
                    firstBagPieceDisplayTopLeftCorner[2],
                    piece.type
                );
            }
        }
    }

    private static void clearHoldPieceBlocks() {
        Utils.fillArea(
                "world",
                holdPieceDisplayTopLeftCorner[0],
                holdPieceDisplayBottomRightCorner[1],
                holdPieceDisplayTopLeftCorner[2],
                holdPieceDisplayBottomRightCorner[0],
                holdPieceDisplayTopLeftCorner[1],
                holdPieceDisplayBottomRightCorner[2],
                Material.AIR
        );
    }

    private void placeHoldPieceBlocks(TetrominoType t) {

        clearHoldPieceBlocks();
        Tetromino piece = new Tetromino(t);

        for (int i = 0; i < 4; i++) {
            Utils.placeTetrominoTypeAsBlock(
                    "world",
                    holdPieceDisplayTopLeftCorner[0] + piece.coords[i][0] + 2,
                    holdPieceDisplayTopLeftCorner[1] + piece.coords[i][1] - 2,
                    holdPieceDisplayTopLeftCorner[2],
                    t
            );
        }
    }

    private boolean checkCollision(int y, int x) {
        boolean collision = false;
        for (int i = 0; i < 4; i++) {
            int coordX = x + currentPiece.coords[i][0];
            int coordY = y + currentPiece.coords[i][1];
            if (board.outOfBounds(coordX, coordY) || board.get(coordX, coordY) != TetrominoType.Empty) {
                collision = true;
                break;
            }
        }
        return collision;
    }

    public void holdPiece() {

        if (hasHoldPiece) return;
        hasHoldPiece = true;

        System.out.println("Hold piece");

        if (holdPieceType == TetrominoType.Empty) {
            holdPieceType = currentPiece.type;
            placeHoldPieceBlocks(holdPieceType);
            justHoldPiece = true;

            Bukkit.getScheduler().cancelTask(Tetris.gameLoopID);
            run();
        } else {
            board.setPieceBlocks(currentPiece, TetrominoType.Empty);

            TetrominoType tmp = holdPieceType;
            holdPieceType = currentPiece.type;
            currentPiece = new Tetromino(tmp);
            placeHoldPieceBlocks(holdPieceType);


            //board.setPieceBlocks(currentPiece, currentPiece.type);
        }

    }

    public void rotateLeft() {
        board.setPieceBlocks(currentPiece, TetrominoType.Empty);
        currentPiece.rotateLeft();
        board.setPieceBlocks(currentPiece, currentPiece.type);
    }

    public void rotateRight() {
        board.setPieceBlocks(currentPiece, TetrominoType.Empty);
        currentPiece.rotateRight();
        board.setPieceBlocks(currentPiece, currentPiece.type);
    }

    public void moveRight() {
        System.out.println(currentPiece.x);
        if (!checkCollision(currentPiece.y, currentPiece.x + 1)) board.movePieceRight(currentPiece);
    }

    public void moveLeft() {
        if (!checkCollision(currentPiece.y, currentPiece.x - 1)) board.movePieceLeft(currentPiece);
    }

    public void moveDown() {
        if (!checkCollision(currentPiece.y - 1, currentPiece.x)) {
            board.movePieceDown(currentPiece);
        }
    }

    public void hardDrop() {
        board.setPieceBlocks(currentPiece, TetrominoType.Empty);
        while (!checkCollision(currentPiece.y - 1, currentPiece.x)) currentPiece.y -= 1;
        board.setPieceBlocks(currentPiece, currentPiece.type);
    }

    public GameLoop(Player player) {
        this.bag                = genBag();
        this.bag2               = genBag();
        this.bagIndex           = 1;
        this.currentPiece       = new Tetromino(bag[0]);
        this.board              = new Board();
        this.player             = player;
        this.justHoldPiece      = false;
        this.hasHoldPiece       = false;
        this.linesClearedCount  = 0;
        this.score              = 0;
        this.level              = 0;

        placeBagPieceDisplayBlocks();
        clearHoldPieceBlocks();
    }

    @Override
    public void run() {

        if (checkCollision(currentPiece.y - 1, currentPiece.x) || justHoldPiece) {
            if (didJustCreatePiece) {
                Bukkit.getScheduler().cancelTask(Tetris.gameLoopID);
                Tetris.isGameGoing = false;
                player.sendMessage(ChatColor.RED + "Game over!");
                return;
            }

            if (justHoldPiece) {
                board.setPieceBlocks(currentPiece, TetrominoType.Empty);
                justHoldPiece = false;
            } else {
                board.setPiece(currentPiece);
                hasHoldPiece = false;
            }

            int linesCleared = board.clearLines(player);
            linesClearedCount += linesCleared;
            score += calculateScore(linesCleared);
            level = linesClearedCount / 10;

            Utils.fillArea("world", -8, 8, -11, -14, 8, -11, Material.AIR);
            writeNumber(linesClearedCount, lineCountStartLoc[0], lineCountStartLoc[1], lineCountStartLoc[2]);

            Utils.fillArea("world", -8, 6, -11, -14, 6, -11, Material.AIR);
            writeNumber(level, levelLoc[0], levelLoc[1], levelLoc[2]);

            Utils.fillArea("world", -8, 15, -11, -14, 15, -11, Material.AIR);
            writeNumber(score, scoreStartLoc[0], scoreStartLoc[1], scoreStartLoc[2]);

            System.out.println("Score: " + score);


            board.setBoardBlocks();

            if (bagIndex == 7) {
                bag = bag2.clone();
                bag2 = genBag();
                bagIndex = 0;
            }
            currentPiece = new Tetromino(bag[bagIndex]);
            bagIndex += 1;
            didJustCreatePiece = true;
            placeBagPieceDisplayBlocks();

        } else {
            didJustCreatePiece = false;

            if (!checkCollision(currentPiece.y - 1, currentPiece.x))
                board.movePieceDown(currentPiece);
        }

        Tetris.gameLoopID = Bukkit.getScheduler().scheduleSyncDelayedTask(
            Tetraft.plugin,
            this,
            10L
        );

    }

}
