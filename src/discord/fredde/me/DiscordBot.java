package discord.fredde.me;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.List;

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

  List<Member> getMembers()
  {
    return textChannel.getMembers();
  }

  void sendFromMinecraft(String message)
  {
    MessageBuilder mb = new MessageBuilder();
    String[] words = message.split(" ");

    for (int i = 0; i < words.length; i++)
    {
      String word = words[i];

      if (word.startsWith("@"))
        for (Member member : getMembers())
          if (word.substring(1).equalsIgnoreCase(member.getEffectiveName()))
          {
            word = member.getAsMention();
            break;
          }

      mb.append(word);

      if(i != words.length - 1)
        mb.append(' ');
    }

    textChannel.sendMessage(mb.build()).queue();
  }
}
