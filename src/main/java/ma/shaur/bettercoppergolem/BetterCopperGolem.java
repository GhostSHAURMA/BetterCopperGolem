package ma.shaur.bettercoppergolem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.shaur.bettercoppergolem.config.ConfigHandler;
import net.fabricmc.api.ModInitializer;

public class BetterCopperGolem implements ModInitializer 
{
	public static final String MOD_ID = "bettercoppergolem";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() 
	{
		ConfigHandler.init();
	}
}