package dev.profsucrose.tetraft.models;

import org.bukkit.Material;

import java.util.EnumMap;

public class Tetromino {
    private static int[][][] coordsTable = new int[][][]{
            /* Z */     { {  0, -1 }, { 0,  0 }, { -1, 0 }, { -1, 1 } },
            /* S */     { {  0, -1 }, { 0,  0 }, {  1, 0 }, {  1, 1 } },
            /* I */     { {  0, -1 }, { 0,  0 }, {  0, 1 }, {  0, 2 } },
            /* T */     { { -1,  0 }, { 0,  0 }, {  1, 0 }, {  0, 1 } },
            /* O */     { {  0,  0 }, { 1,  0 }, {  0, 1 }, {  1, 1 } },
            /* L */     { { -1, -1 }, { 0, -1 }, {  0, 0 }, {  0, 1 } },
            /* J */     { {  1, -1 }, { 0, -1 }, {  0, 0 }, {  0, 1 } }
    };

    public int[][] coords = new int[4][2];
    public int y = 20;
    public int x = 4;
    public TetrominoType type;

    public Tetromino(TetrominoType type) {
        this.coords = coordsTable[type.ordinal()];
        this.type = type;
    }
}
