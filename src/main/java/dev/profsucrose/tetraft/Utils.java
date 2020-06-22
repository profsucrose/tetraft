package dev.profsucrose.tetraft;

import dev.profsucrose.tetraft.models.TetrominoType;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.ArrayList;
import java.util.List;

public class Utils {
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

    public static ArrayList<List<Pattern>> numberBannerPatterns = new ArrayList<>();

    private static List<Pattern> createPattern(PatternType[] patternTypes, DyeColor[] colors) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (int i = 0; i < patternTypes.length; i++) patterns.add(new Pattern(colors[i], patternTypes[i]));
        return patterns;
    }

    public static void initPatterns() {
        numberBannerPatterns.add(createPattern(
            new PatternType[] { PatternType.STRIPE_BOTTOM, PatternType.STRIPE_LEFT, PatternType.STRIPE_TOP, PatternType.STRIPE_RIGHT, PatternType.DIAGONAL_RIGHT, PatternType.BORDER },
            new DyeColor[]    { DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.BLACK, DyeColor.WHITE }
        ));

        System.out.println("Assigned banner patterns");
        System.out.println(numberBannerPatterns.get(0));
    }

    static private Material tetrominoTypeToBlock(TetrominoType t) {
        return blockTable[t.ordinal()];
    }

    static public void fillArea(String worldName, int fromX, int fromY, int fromZ, int toX, int toY, int toZ, Material material) {
        for (int x = fromX; x <= toX; x++) {
            for (int y = fromY; y <= toY; y++) {
                for (int z = fromZ; z <= toZ; z++) {
                    Bukkit.getWorld(worldName).getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }

    static public void placeTetrominoTypeAsBlock(String worldName, int x, int y, int z, TetrominoType t) {
        Bukkit.getWorld(worldName).getBlockAt(x, y, z).setType(tetrominoTypeToBlock(t));
    }
}
