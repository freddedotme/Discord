package discord.fredde.me;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class Config
{
	private static final Gson GSON = new Gson();
	
	private String botTokenId;
	private long textChannelId;
	
	public void defaultSettings()
	{
		botTokenId = "";
		textChannelId = -1;
	}
	
	public static Config load() throws FileNotFoundException
	{
		return GSON.fromJson(new FileReader("Discord_config.json"), Config.class);
	}
	
	public void save(String fileName) throws IOException
	{
		GSON.toJson(this, new FileWriter(fileName));
	}
	
	public String getBotTokenID()
	{
		return botTokenId;
	}
	
	public long getTextChannelID()
	{
		return textChannelId;
	}
}
