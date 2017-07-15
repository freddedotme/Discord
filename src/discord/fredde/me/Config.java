package discord.fredde.me;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

final class Config
{
  private static final Gson   GSON      = new GsonBuilder().setPrettyPrinting().create();
  private static final String FILE_NAME = "plugins/Discord_config.json";

  private String botTokenId;
  private long   textChannelId;

  void defaultSettings()
  {
    botTokenId = "";
    textChannelId = -1;
  }

  static Config load() throws FileNotFoundException
  {
    return GSON.fromJson(new FileReader(FILE_NAME), Config.class);
  }

  void save() throws IOException
  {
    GSON.toJson(this, new FileWriter(FILE_NAME));
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
