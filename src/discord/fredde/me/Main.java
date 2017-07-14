package discord.fredde.me;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Main extends JavaPlugin implements Listener
{
  @Override
  public void onEnable()
  {
    super.onEnable();
    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
  {
    pushMessage(e.getPlayer().getName(), e.getMessage());
  }

  private void pushMessage(String name, String message)
  {
    new BukkitRunnable()
    {
      @Override
      public void run()
      {
        Content c = new Content();
        c.setContent(name + ": " + message);

        String postUrl = "WEBHOOK";
        Gson         gson          = new Gson();
        HttpClient   httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(postUrl);
        StringEntity postingString = null;
        try
        {
          postingString = new StringEntity(gson.toJson(c));
        } catch (UnsupportedEncodingException e)
        {
          e.printStackTrace();
        }
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        try
        {
          HttpResponse response = httpClient.execute(post);
        } catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }.runTaskAsynchronously(this);
  }

  class Content
  {
    String content;

    String getContent()
    {
      return content;
    }

    void setContent(String content)
    {
      this.content = content;
    }
  }
}
