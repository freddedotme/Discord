package discord.fredde.me;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class Config
{
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private String botTokenId;
  private long   textChannelId;

  void defaultSettings()
  {
    botTokenId = "";
    textChannelId = -1;
  }

  static Config load() throws FileNotFoundException
  {
    return GSON.fromJson(new FileReader("Discord_config.json"), Config.class);
  }

  public void save(String fileName) throws IOException
  {
    GSON.toJson(this, new FileWriter(fileName));
  }

  String getBotTokenID()
  {
    return botTokenId;
  }

  long getTextChannelID()
  {
    return textChannelId;
  }
}
