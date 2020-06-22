package dev.profsucrose.tetraft.models;

import org.bukkit.Material;

import java.util.EnumMap;

public class Tetromino {
    private static final int[][][] coordsTable = new int[][][]{
            /* Z */     { {  0, -1 }, {  0,  0 }, { -1, 0 }, { -1, 1 } },
            /* S */     { {  0, -1 }, {  0,  0 }, {  1, 0 }, {  1, 1 } },
            /* I */     { {  0, -1 }, {  0,  0 }, {  0, 1 }, {  0, 2 } },
            /* T */     { { -1,  0 }, {  0,  0 }, {  1, 0 }, {  0, 1 } },
            /* O */     { {  0,  0 }, {  1,  0 }, {  0, 1 }, {  1, 1 } },
            /* L */     { { -1, -1 }, {  0, -1 }, {  0, 0 }, {  0, 1 } },
            /* J */     { { -1,  -1 }, { -1, 0 }, {  0, 0 }, {  1, 0 } }
    };

    public int[][] coords;
    public int y = 17;
    public int x = 4;
    public TetrominoType type;

    public void rotateLeft() {
        if (type == TetrominoType.O) return;
        for (int i = 0; i < 4; i++) {
            int tmp = coords[i][0];
            coords[i][0] = -coords[i][1];
            coords[i][1] = tmp;
        }
    }

    public void rotateRight() {
        if (type == TetrominoType.O) return;
        for (int i = 0; i < 4; i++) {
            int tmp = coords[i][0];
            coords[i][0] = coords[i][1];
            coords[i][1] = -tmp;
        }
    }

    public Tetromino(TetrominoType type) {
        this.coords = new int[4][2];
        for (int i = 0; i < 4; i++) {
            this.coords[i] = coordsTable[type.ordinal()][i].clone();
        }
        this.type = type;
    }
}
