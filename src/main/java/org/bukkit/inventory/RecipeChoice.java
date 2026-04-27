package org.bukkit.inventory;

import java.util.List;
import org.bukkit.Material;

public class RecipeChoice {
    private final List<Material> choices;

    public RecipeChoice(Material choice) {
        this.choices = java.util.Collections.singletonList(choice);
    }

    public RecipeChoice(List<Material> choices) {
        this.choices = choices;
    }

    public List<Material> getChoices() {
        return choices;
    }

    public ItemStack getItemStack() {
        return new ItemStack(choices.get(0));
    }

    public static class MaterialChoice extends RecipeChoice {
        public MaterialChoice(Material choice) {
            super(choice);
        }

        public MaterialChoice(List<Material> choices) {
            super(choices);
        }
    }
}
