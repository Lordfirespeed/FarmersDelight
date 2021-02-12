package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.utils.ListUtils;

@ZenRegister
@ZenCodeType.Name("mods.farmersdelight.CookingPot")
public class CookingPotRecipeManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name,
                          IItemStack output,
                          IIngredient[] inputs,
                          @ZenCodeType.Optional IItemStack container,
                          @ZenCodeType.OptionalFloat float experience,
                          @ZenCodeType.OptionalInt(200) int cookTime) {
        if (!validateInputs(inputs)) return;

        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new CookingPotRecipe(new ResourceLocation(CraftTweaker.MODID, name),
                        "",
                        ListUtils.mapArrayIndexSet(inputs,
                                IIngredient::asVanillaIngredient,
                                NonNullList.withSize(inputs.length, Ingredient.EMPTY)),
                        output.getInternal(),
                        container == null ? ItemStack.EMPTY : container.getInternal(),
                        experience,
                        cookTime),
                ""));
    }

    private boolean validateInputs(IIngredient[] inputs) {
        if (inputs.length == 0) {
            CraftTweakerAPI.logError("No ingredients for cooking recipe");
            return false;
        } else if (inputs.length > CookingPotRecipe.INPUT_SLOTS) {
            CraftTweakerAPI.logError("Too many ingredients for cooking recipe! The max is %s", CookingPotRecipe.INPUT_SLOTS);
            return false;
        }
        return true;
    }

    @Override
    public IRecipeType<CookingPotRecipe> getRecipeType() {
        return CookingPotRecipe.TYPE;
    }
}
