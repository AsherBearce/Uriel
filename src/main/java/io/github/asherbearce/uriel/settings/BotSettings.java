package io.github.asherbearce.uriel.settings;

public class BotSettings {
    private String botToken;

    public String getBotToken() { return botToken; }
    public void setBotToken(String token) { botToken = token; }

    public BotSettings(String token){
        botToken = token;
    }

    public BotSettings(){

    }
}
