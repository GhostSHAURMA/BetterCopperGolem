package ma.shaur.bettercoppergolem.config.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.minecraft.client.gui.screens.Screen;

public class ModMenu implements ModMenuApi 
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() 
	{
		return this::setupConfigScreen;
	}
	
	public Screen setupConfigScreen(Screen parent)
	{
		return new ConfigScreen(parent);
	}
}
