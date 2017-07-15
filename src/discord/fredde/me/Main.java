package discord.fredde.me;

import net.dv8tion.jda.core.entities.Member;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends JavaPlugin implements Listener
{
  private DiscordBot bot;

  @Override
  public void onEnable()
  {
    getServer().getPluginManager().registerEvents(this, this);

    Config config;
    try
    {
      config = Config.load();
      bot = new DiscordBot(config, this);
    } catch (FileNotFoundException e)
    {
      config = new Config();
      config.defaultSettings();

      try
      {
        config.save();
      } catch (IOException e1)
      {
        e1.printStackTrace();
      }

      getLogger()
        .warning("[Discord] Created config file, please open it and set up the Discord bot.");
    }
  }

  public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
  {
    if (commandSender instanceof Player)
    {
      Player player = (Player) commandSender;
      if (s.equalsIgnoreCase("discord"))
      {
        player.sendMessage(cc("&9Invite link: &ahttps://discord.gg/azK8vdY"));
        player.sendMessage(cc("&9Members on Discord:"));

        if (bot.getMembers().size() > 0)
        {
          for (Member member : bot.getMembers())
          {
            if (!member.getOnlineStatus().toString().equalsIgnoreCase("OFFLINE"))
            {
              String m = member.getEffectiveName();
              m += " (" + member.getOnlineStatus().toString().toLowerCase() + ")";
              if (member.getGame() != null) m += " [" + member.getGame() + "]";
              player.sendMessage(m);
            }
          }
        }
        else
        {
          player.sendMessage("None online");
        }

        return true;
      }
    }
    return false;
  }

  @EventHandler
  public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
  {
    pushMessage(e.getPlayer().getName() + ": " + e.getMessage());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e)
  {
    pushMessage(e.getPlayer().getName() + " joined.");
    pushMessage("Players online: " + String.valueOf(getServer().getOnlinePlayers().size()));
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e)
  {
    pushMessage(e.getPlayer().getName() + " left.");
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e)
  {
    pushMessage(e.getDeathMessage());
  }

  private void pushMessage(String message)
  {
    if (bot != null)
      bot.sendFromMinecraft(message);
  }

  void sendFromDiscord(String name, String message)
  {
    getServer().broadcastMessage(cc("<&9Discord&r " + name + "> " + message));
  }

  private String cc(String message)
  {
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}
