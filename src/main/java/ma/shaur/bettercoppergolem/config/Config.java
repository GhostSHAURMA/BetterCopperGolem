package ma.shaur.bettercoppergolem.config;

@SuppressWarnings("unused")
public class Config 
{
	private final String _comment = "Download mod menu for better config experience and option explanations with translation";
	
	public boolean shulkerAndBundleSorting = true;
	
	public boolean ignoreColor = false;

	public boolean allowIndividualItemsMatchContainerContents = false;

	public boolean allowInsertingItemsIntoContainers = false;

	public boolean matchOxidationLevel = false;

	public int maxChestCheckCount = 10;

	public int maxHeldItemStackSize = 16;

	public int cooldownTime = 140;
	
	public int verticalRange = 2;
	
	public int horizontalRange = 32;

	public int interactionTime = 60;
	
	public Config() {}
}
