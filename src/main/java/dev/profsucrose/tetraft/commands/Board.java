package dev.profsucrose.tetraft.commands;

import dev.profsucrose.tetraft.Tetraft;
import dev.profsucrose.tetraft.Utils;
import dev.profsucrose.tetraft.models.Tetromino;
import dev.profsucrose.tetraft.models.TetrominoType;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class Board {
    private static int[] wellBottomLeftCornerXYZ = new int[] { -5, 5, -12 };
    public TetrominoType[][] board = new TetrominoType[20][10];

    public void setBlock(int x, int y, TetrominoType t) {
        Utils.placeTetrominoTypeAsBlock(
                "world",
                wellBottomLeftCornerXYZ[0] + x,
                wellBottomLeftCornerXYZ[1] + y,
                wellBottomLeftCornerXYZ[2],
                t
        );
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

    public int clearLines(Player player) {
        int linesCleared = 0;
        for (int y = 19; y >= 0; y--) {
            boolean lineCleared = true;
            for (int x = 0; x < 10; x++) {
                if (get(x, y) == TetrominoType.Empty) {
                    lineCleared = false;
                    break;
                }
            }

            if (lineCleared) {
                linesCleared++;

                for (int k = y; k < 19; k++) {
                    for (int j = 0; j < 10; j++) {
                        board[k][j] = board[k + 1][j];
                    }
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(Tetraft.plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.playNote(player.getEyeLocation(), Instrument.BIT, Note.flat(1, Note.Tone.A));
                    }
                }, (long) linesCleared * 5);
            }
        }

        return linesCleared;
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

    public void clearBoard() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++)
                set(x, y, TetrominoType.Empty);
        }
    }

    public void clearBoardBlocks() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++)
                setBlock(x, y, TetrominoType.Empty);
        }
    }

    public void setBoardBlocks() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 10; x++)
                setBlock(x, y, get(x, y));
        }
    }

    public Board() {
        clearBoard();
        clearBoardBlocks();
    }
}
