package city.norain.slimefun4.compatibillty;

import com.cryptomorin.xseries.XMaterial;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class LegacyTagCompat {

    private static final Set<Material> WOOL = materials(
            "WHITE_WOOL", "ORANGE_WOOL", "MAGENTA_WOOL", "LIGHT_BLUE_WOOL",
            "YELLOW_WOOL", "LIME_WOOL", "PINK_WOOL", "GRAY_WOOL",
            "LIGHT_GRAY_WOOL", "CYAN_WOOL", "PURPLE_WOOL", "BLUE_WOOL",
            "BROWN_WOOL", "GREEN_WOOL", "RED_WOOL", "BLACK_WOOL"
    );

    private static final Set<Material> LOGS = materials(
            "OAK_LOG", "SPRUCE_LOG", "BIRCH_LOG", "JUNGLE_LOG",
            "ACACIA_LOG", "DARK_OAK_LOG",
            "OAK_WOOD", "SPRUCE_WOOD", "BIRCH_WOOD", "JUNGLE_WOOD",
            "ACACIA_WOOD", "DARK_OAK_WOOD",
            "STRIPPED_OAK_LOG", "STRIPPED_SPRUCE_LOG", "STRIPPED_BIRCH_LOG", "STRIPPED_JUNGLE_LOG",
            "STRIPPED_ACACIA_LOG", "STRIPPED_DARK_OAK_LOG"
    );

    private static final Set<Material> PLANKS = materials(
            "OAK_PLANKS", "SPRUCE_PLANKS", "BIRCH_PLANKS", "JUNGLE_PLANKS",
            "ACACIA_PLANKS", "DARK_OAK_PLANKS"
    );

    private static final Set<Material> LEAVES = materials(
            "OAK_LEAVES", "SPRUCE_LEAVES", "BIRCH_LEAVES", "JUNGLE_LEAVES",
            "ACACIA_LEAVES", "DARK_OAK_LEAVES"
    );

    private static final Set<Material> SAND = materials(
            "SAND", "RED_SAND"
    );

    private static final Set<Material> ICE = materials(
            "ICE", "PACKED_ICE", "BLUE_ICE", "FROSTED_ICE"
    );

    private static final Set<Material> CORAL_BLOCKS = materials(
            "TUBE_CORAL_BLOCK", "BRAIN_CORAL_BLOCK", "BUBBLE_CORAL_BLOCK",
            "FIRE_CORAL_BLOCK", "HORN_CORAL_BLOCK"
    );

    private static final Set<Material> BASE_STONE_OVERWORLD = materials(
            "STONE", "GRANITE", "DIORITE", "ANDESITE"
    );

    private static final Set<Material> BASE_STONE_NETHER = materials(
            "NETHERRACK", "BASALT", "BLACKSTONE", "NETHER_BRICKS"
    );

    private static final Set<Material> COALS = materials(
            "COAL", "CHARCOAL"
    );

    private static final Set<Material> WOODEN_TRAPDOORS = materials(
            "OAK_TRAPDOOR", "SPRUCE_TRAPDOOR", "BIRCH_TRAPDOOR", "JUNGLE_TRAPDOOR",
            "ACACIA_TRAPDOOR", "DARK_OAK_TRAPDOOR", "IRON_TRAPDOOR"
    );

    private static final Set<Material> WOODEN_SLABS = materials(
            "OAK_SLAB", "SPRUCE_SLAB", "BIRCH_SLAB", "JUNGLE_SLAB",
            "ACACIA_SLAB", "DARK_OAK_SLAB", "STONE_SLAB", "COBBLESTONE_SLAB"
    );

    private static final Set<Material> WOODEN_FENCES = materials(
            "OAK_FENCE", "SPRUCE_FENCE", "BIRCH_FENCE", "JUNGLE_FENCE",
            "ACACIA_FENCE", "DARK_OAK_FENCE"
    );

    private static final Set<Material> FIRE = materials("FIRE");

    public static Set<Material> getWool() { return WOOL; }
    public static Set<Material> getLogs() { return LOGS; }
    public static Set<Material> getPlanks() { return PLANKS; }
    public static Set<Material> getLeaves() { return LEAVES; }
    public static Set<Material> getSand() { return SAND; }
    public static Set<Material> getIce() { return ICE; }
    public static Set<Material> getCoralBlocks() { return CORAL_BLOCKS; }
    public static Set<Material> getBaseStoneOverworld() { return BASE_STONE_OVERWORLD; }
    public static Set<Material> getBaseStoneNether() { return BASE_STONE_NETHER; }
    public static Set<Material> getCoals() { return COALS; }
    public static Set<Material> getWoodenTrapdoors() { return WOODEN_TRAPDOORS; }
    public static Set<Material> getWoodenSlabs() { return WOODEN_SLABS; }
    public static Set<Material> getWoodenFences() { return WOODEN_FENCES; }
    public static Set<Material> getFire() { return FIRE; }

    public static boolean isTagged(String tagName, Material material) {
        switch (tagName) {
            case "WOOL": return WOOL.contains(material);
            case "LOGS": return LOGS.contains(material);
            case "PLANKS": return PLANKS.contains(material);
            case "LEAVES": return LEAVES.contains(material);
            case "SAND": return SAND.contains(material);
            case "ICE": return ICE.contains(material);
            case "CORAL_BLOCKS": return CORAL_BLOCKS.contains(material);
            case "BASE_STONE_OVERWORLD": return BASE_STONE_OVERWORLD.contains(material);
            case "BASE_STONE_NETHER": return BASE_STONE_NETHER.contains(material);
            case "COALS": return COALS.contains(material);
            case "WOODEN_TRAPDOORS": return WOODEN_TRAPDOORS.contains(material);
            case "WOODEN_SLABS": return WOODEN_SLABS.contains(material);
            case "WOODEN_FENCES": return WOODEN_FENCES.contains(material);
            case "FIRE": return FIRE.contains(material);
            default: return false;
        }
    }

    private static Set<Material> materials(String... names) {
        Set<Material> result = new HashSet<>();
        for (String name : names) {
            XMaterial xmat = XMaterial.matchXMaterial(name).orElse(null);
            if (xmat != null) {
                Material mat = xmat.parseMaterial();
                if (mat != null) {
                    result.add(mat);
                }
            }
        }
        return Collections.unmodifiableSet(result);
    }
}
