package discord.fredde.me;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;

public class Main extends JavaPlugin implements Listener
{
	private Config config;
	private DiscordBot bot;
	
	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onLoad()
	{
		try
		{
			config = Config.load();
			bot = new DiscordBot(config, this);
		}
		catch(FileNotFoundException e)
		{
			config = new Config();
			config.defaultSettings();
			
			getServer().getLogger().warning("[Discord] Created config file, please open it and set up the Discord bot.");
		}
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
	{
		pushMessage(e.getPlayer().getName(), e.getMessage());
	}
	
	private void pushMessage(String name, String message)
	{
		bot.sendFromMinecraft(name, message);
	}
	
	public void sendFromDiscord(String name, String message)
	{
		getServer().broadcastMessage("<" + ChatColor.BLUE + "Discord" + ChatColor.RESET + name + "> " + message);
	}
}
