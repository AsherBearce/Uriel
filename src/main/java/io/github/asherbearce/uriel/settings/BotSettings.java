package io.github.asherbearce.uriel.settings;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.LinkedList;
import java.util.List;

public class BotSettings {

    private enum CHANGE_TYPE{
        PREFIX,
        MUTEROLEID,
        TOKEN
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
        notifyEvent(CHANGE_TYPE.PREFIX);
    }

    public String getBotToken() {
        return this.botToken;
    }
    public void setBotToken(String token) {
        this.botToken = token;
        notifyEvent(CHANGE_TYPE.TOKEN);
    }

    public String getMutedRoleID() {
        return mutedRoleID;
    }

    public void setMutedRoleID(String mutedRoleID) {
        this.mutedRoleID = mutedRoleID;
        notifyEvent(CHANGE_TYPE.MUTEROLEID);
    }

    public String getLogChannelID() {
        return logChannelID;
    }

    public void setLogChannelID(String logChannelID) {
        this.logChannelID = logChannelID;
    }

    public List<String> getBlacklistWords() {
        return blacklistWords;
    }

    public void setBlacklistWords(List<String> blacklistWords) {
        this.blacklistWords = blacklistWords;
    }

    public void setSpamChannelIds(List<String> spamChannels) { this.spamChannelIds = spamChannels; }

    public List<String> getSpamChannelIds() { return spamChannelIds; }

    public void addSpamChannel(String id) { this.spamChannelIds.add(id); }

    private String botToken;
    private String commandPrefix;
    private String mutedRoleID;
    private String logChannelID;
    private List<SettingsChangedEventHandler> events = new LinkedList<>();
    private List<String> blacklistWords = new LinkedList<>();
    private List<String> spamChannelIds = new LinkedList<>();

    private void notifyEvent(CHANGE_TYPE type){
        for (SettingsChangedEventHandler handler : events){
            if (type == CHANGE_TYPE.PREFIX){
                handler.onCommandPrefixChanged();
            } else if (type == CHANGE_TYPE.MUTEROLEID){
                handler.onMutedRoleIDChanged();
            } else if (type == CHANGE_TYPE.TOKEN){
                handler.onBotTokenChanged();
            }
        }
    }

    private void initializeBlacklist(){
        blacklistWords.add("tranny");
        blacklistWords.add("shemale");
        blacklistWords.add("ladyboy");
        blacklistWords.add("fag");
        blacklistWords.add("faggot");
        blacklistWords.add("pedo");
        blacklistWords.add("pedophile");
        blacklistWords.add("molest");
        blacklistWords.add("molestation");
        blacklistWords.add("nigga");
        blacklistWords.add("nigger");
        blacklistWords.add("n1gga");
        blacklistWords.add("n1gger");
    }

    public BotSettings(String token){
        botToken = token;
        commandPrefix = "!";
        initializeBlacklist();
        spamChannelIds = new LinkedList<>();
    }

    public BotSettings(){
        commandPrefix = "!";
        initializeBlacklist();
        spamChannelIds = new LinkedList<>();
    }

    public void registerEventHandler(SettingsChangedEventHandler handler){
        events.add(handler);
    }

    public interface SettingsChangedEventHandler{
        default void onBotTokenChanged(){}
        default void onCommandPrefixChanged(){}
        default void onMutedRoleIDChanged(){}
    }
}
