package ma.shaur.bettercoppergolem.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ma.shaur.bettercoppergolem.custom.entity.LastItemDataHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.coppergolem.CopperGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@Mixin(CopperGolem.class)
public class CopperGolemMixin implements LastItemDataHolder
{
	//An item for optimizing selection(select same type of items first for faster sorting and item batching
	//Maybe move to CopperGolemBrain an store as a memory? 
	private ItemStack lastItemStack = ItemStack.EMPTY;

	@Inject(method = "addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V", at = @At("TAIL"))
	public void writeCustomData(ValueOutput view, CallbackInfo info) 
	{
		view.store("last_item_stack", ItemStack.OPTIONAL_CODEC, lastItemStack);
	}

	@Inject(method = "readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V", at = @At("TAIL"))
	public void readCustomData(ValueInput view, CallbackInfo info)
	{
		view.read("last_item_stack", ItemStack.OPTIONAL_CODEC);
	}
	
	@Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/coppergolem/CopperGolem;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER))
	public void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info)
	{
		//No need to optimize for the same item since a player took it out of golem's hands
		lastItemStack = ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getLastItemStack() 
	{
		return lastItemStack;
	}

	@Override
	public void setLastItemStack(ItemStack lastItemStack) 
	{
		this.lastItemStack = lastItemStack;
	}
}
