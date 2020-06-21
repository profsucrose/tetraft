package dev.profsucrose.tetraft.commands;

import dev.profsucrose.tetraft.models.Tetromino;
import dev.profsucrose.tetraft.models.TetrominoType;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class Board {
    private static int[] wellBottomLeftCornerXYZ = new int[] { -5, 5, -12 };
    private static Material[] blockTable = new Material[]{
            /* Z    */     Material.RED_CONCRETE,
            /* S    */     Material.LIME_CONCRETE,
            /* I    */     Material.LIGHT_BLUE_CONCRETE,
            /* T    */     Material.PURPLE_CONCRETE,
            /* O    */     Material.YELLOW_CONCRETE,
            /* L    */     Material.ORANGE_CONCRETE,
            /* J    */     Material.BLUE_CONCRETE,
            /* None */     Material.AIR
    };

    private TetrominoType[][] board = new TetrominoType[20][10];

    private static Material typeToMaterial(TetrominoType t) {
        return blockTable[t.ordinal()];
    }

    public void setBlock(int x, int y, TetrominoType t) {
        Bukkit.getWorld("world").getBlockAt(
                wellBottomLeftCornerXYZ[0] + x,
                wellBottomLeftCornerXYZ[1] + y,
                wellBottomLeftCornerXYZ[2]
        ).setType(typeToMaterial(t));
    }

    public void set(int x, int y, TetrominoType t) {
        board[y][x] = t;
    }

    public boolean outOfBounds(int x, int y) {
        return y < 0 || y >= 20 || x < 0 || x >= 10;
    }

    public TetrominoType get(int x, int y) {
        return board[y][x];
    }

    public void setPieceBlocks(Tetromino piece, TetrominoType type) {
        for (int i = 0; i < 4; i++) {
            int coordX = piece.x + piece.coords[i][0];
            int coordY = piece.y + piece.coords[i][1];
            setBlock(coordX, coordY, type);
        }
    }

    public void movePieceDown(Tetromino piece) {
        setPieceBlocks(piece, TetrominoType.Empty);

        piece.y -= 1;
        setPieceBlocks(piece, piece.type);
    }

    public void movePieceRight(Tetromino piece) {
        setPieceBlocks(piece, TetrominoType.Empty);

        piece.x += 1;
        setPieceBlocks(piece, piece.type);
    }

    public void movePieceLeft(Tetromino piece) {
        setPieceBlocks(piece, TetrominoType.Empty);

        piece.x -= 1;
        setPieceBlocks(piece, piece.type);
    }

    public void setPiece(Tetromino piece) {
        for (int i = 0; i < 4; i++) {
            int coordX = piece.x + piece.coords[i][0];
            int coordY = piece.y + piece.coords[i][1];
            set(coordX, coordY, piece.type);
        }
    }

    public Board() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++) {
                setBlock(x, y, TetrominoType.Empty);
                set(x, y, TetrominoType.Empty);
            }
        }
    }
}
