package discord.fredde.me;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public final class DiscordBot extends ListenerAdapter
{
  private final Config      config;
  private final Main        main;
  private       TextChannel textChannel;

  DiscordBot(Config config, Main main)
  {
    this.config = config;
    this.main = main;

    try
    {
      new JDABuilder(AccountType.BOT)
        .setToken(config.getBotTokenID())
        .addEventListener(this)
        .buildAsync();
    } catch (LoginException e)
    {
      e.printStackTrace();
    } catch (RateLimitedException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void onReady(ReadyEvent event)
  {
    textChannel = event.getJDA().getTextChannelById(config.getTextChannelID());
  }

  @Override
  public void onGuildMessageReceived(GuildMessageReceivedEvent event)
  {
    if (event.getChannel().getIdLong() == config.getTextChannelID() && !event.getAuthor().isBot())
    {
      String name    = event.getMember().getEffectiveName();
      String message = event.getMessage().getContent();
      main.sendFromDiscord(name, message);
    }
  }

  void sendFromMinecraft(String name, String message)
  {
    MessageBuilder mb = new MessageBuilder();
    mb.append(name);
    mb.append(": ");
    mb.append(message);

    textChannel.sendMessage(mb.build()).queue();
  }
}
