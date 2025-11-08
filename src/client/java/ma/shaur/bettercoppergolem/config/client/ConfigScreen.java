package ma.shaur.bettercoppergolem.config.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.JsonIOException;

import ma.shaur.bettercoppergolem.BetterCopperGolemClient;
import ma.shaur.bettercoppergolem.config.Config;
import ma.shaur.bettercoppergolem.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.OptionInstance.TooltipSupplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.StringWidget.TextOverflow;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ConfigScreen extends OptionsSubScreen 
{
	private static final Predicate<String> ONLY_INTEGER_NUMBERS_PREDICATE = (s) ->
	{
		try
		{
			Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		
		return true;
	};

	@Override
	protected void addFooter() 
	{
		this.layout.addToFooter(new Container(308, 20, List.of(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).width(150).build(), Button.builder(Component.translatable("bettercoppergolem.options.reset"), (button) -> this.setDefaultValues()).width(150).build()), 8));
	}
	
	private void setDefaultValues()
	{
		Config config = ConfigHandler.getConfig();
		Config defaultValues = new Config();

		config.shulkerAndBundleSorting = defaultValues.shulkerAndBundleSorting;
		config.ignoreColor = defaultValues.ignoreColor;
		config.allowIndividualItemsMatchContainerContents = defaultValues.allowIndividualItemsMatchContainerContents;
		config.allowInsertingItemsIntoContainers = defaultValues.allowInsertingItemsIntoContainers;
		config.maxChestCheckCount = defaultValues.maxChestCheckCount;
		config.maxHeldItemStackSize = defaultValues.maxHeldItemStackSize;
		config.cooldownTime = defaultValues.cooldownTime;
		config.verticalRange = defaultValues.verticalRange;
		config.interactionTime = defaultValues.interactionTime;

		try
		{
			ConfigHandler.saveConfig();
		} 
		catch (JsonIOException | IOException e)
		{
			BetterCopperGolemClient.LOGGER.error("Error saving config from a config screen: ", e);
		}

		removeWidget(list);
		addContents();
		addRenderableWidget(list);
		repositionElements();
	}
	
	public ConfigScreen(Screen parent) 
	{
		super(parent, Minecraft.getInstance().options, Component.translatable("bettercoppergolem.options"));
	}

	@Override
	protected void addOptions() 
	{
		if(list == null) return;
		
		Config config = ConfigHandler.getConfig();
		list.addSmall(OptionInstance.createBoolean(translationKey("shulker_and_bundle_sorting"), tooltipFactory("shulker_and_bundle_sorting"), config.shulkerAndBundleSorting, b -> config.shulkerAndBundleSorting = b),
					  OptionInstance.createBoolean(translationKey("ignore_color"), tooltipFactory("ignore_color"), config.ignoreColor, b -> config.ignoreColor = b),
					  OptionInstance.createBoolean(translationKey("allow_individual_items_match_container_contents"), tooltipFactory("allow_individual_items_match_container_contents"), config.allowIndividualItemsMatchContainerContents, b -> config.allowIndividualItemsMatchContainerContents = b),
					  OptionInstance.createBoolean(translationKey("allow_inserting_items_into_containers"), tooltipFactory("allow_inserting_items_into_containers"), config.allowInsertingItemsIntoContainers, b -> config.allowInsertingItemsIntoContainers = b));
		
		list.addSmall(intContainer(Component.translatable(translationKey("max_chest_check_count")),Component.translatable(translationKey("max_chest_check_count.info")), config.maxChestCheckCount, i -> config.maxChestCheckCount = i),
					  intContainer(Component.translatable(translationKey("max_held_item_stack_size")), Component.translatable(translationKey("max_held_item_stack_size.info")), config.maxHeldItemStackSize, i -> config.maxHeldItemStackSize = i));
		list.addSmall(intContainer(Component.translatable(translationKey("cooldown_time")),Component.translatable(translationKey("cooldown_time.info")), config.cooldownTime, i -> config.cooldownTime = i), 
					  intContainer(Component.translatable(translationKey("vertical_range")), Component.translatable(translationKey("vertical_range.info")), config.verticalRange, i -> config.verticalRange = i));
		list.addSmall(intContainer(Component.translatable(translationKey("interaction_time")),Component.translatable(translationKey("interaction_time.info")), config.interactionTime, i -> config.interactionTime = i),
					  OptionInstance.createBoolean(translationKey("match_oxidation_level"), tooltipFactory("match_oxidation_level"), config.matchOxidationLevel, b -> config.matchOxidationLevel = b).createButton(options));
	}
	
	private Container intContainer(Component text, Component tooltip, int value, ChangeListener listener)
	{
		StringWidget textWidget = new StringWidget(100, 20, text, font);
		textWidget.setMaxWidth(0, TextOverflow.SCROLLING);
		
		EditBox field = new EditBox(font, 50, 20, CommonComponents.EMPTY);
		field.setValue(Integer.toString(value));
		field.setCentered(true);
		field.setFilter(ONLY_INTEGER_NUMBERS_PREDICATE);
		field.setResponder(s -> 
		{
			try
			{
				listener.changed(Integer.parseInt(s));
			}
			catch (NumberFormatException e) {}
		});

		Container layout = new Container(150, 20, List.of(textWidget, field));
		layout.setTooltip(Tooltip.create(tooltip));
		
		return layout;
	}

	@Override
	public void removed() 
	{
		try
		{
			ConfigHandler.saveConfig();
		} 
		catch (JsonIOException | IOException e)
		{
			BetterCopperGolemClient.LOGGER.error("Error saving config from a config screen: ", e);
		}
	}
	
	private static TooltipSupplier<Boolean> tooltipFactory(String id) 
	{
		id = translationKey(id);
		MutableComponent text = Component.translatableWithFallback(id + ".info", "");
		Component infoTrue = Component.translatableWithFallback(id + ".info.true", "");
		Component infoFalse = Component.translatableWithFallback(id + ".info.false", "");

		if(!infoTrue.getString().isEmpty()) text.append("\n").append(infoTrue);
		if(!infoFalse.getString().isEmpty()) text.append("\n").append(infoFalse);
		
		return (b) -> text.getString().isEmpty() ? null : Tooltip.create(text);
	}

	private static String translationKey(String name)
	{
		return "option.bettercoppergolem." + name;
	}

	private class Container extends AbstractContainerWidget 
	{
		private List<AbstractWidget> children = new ArrayList<>();
		private int spacing;

	    public Container(int width, int height, List<AbstractWidget> children) 
		{
	    	this(width, height, children, 0);
		}

	    public Container(int width, int height, List<AbstractWidget> children, int spacing) 
		{
			super(0, 0, width, height, CommonComponents.EMPTY);
			this.children = children;
			this.spacing = spacing;
		}

		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaTicks)
		{
			guiGraphics.enableScissor(getX(), getY(), getX() + width, getY() + height);

			int x = getX(), y = getY();
			for(AbstractWidget widget : children) 
			{
				widget.setPosition(x, y);
				widget.render(guiGraphics, mouseX, mouseY, deltaTicks);
				x += widget.getWidth() + spacing;
			}

			guiGraphics.disableScissor();
		}

		@Override
		public List<? extends GuiEventListener> children() 
		{
			return children;
		}
		
		@Override
		public void setFocused(boolean focused) 
		{
			super.setFocused(focused);
			if(!focused) for(AbstractWidget widget : children) widget.setFocused(false);
		}

		@Override
		protected int contentHeight() 
		{
			return getHeight();
		}

		@Override
		protected double scrollRate() 
		{
			return 0;
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) { }
	}
	
	private interface ChangeListener
	{
		void changed(int i);
	}
}
