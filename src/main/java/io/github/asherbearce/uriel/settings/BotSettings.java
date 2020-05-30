package io.github.asherbearce.uriel.settings;

import net.dv8tion.jda.api.entities.TextChannel;

public class BotSettings {


    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public String getBotToken() {
        return this.botToken;
    }
    public void setBotToken(String token) {
        this.botToken = token;
    }

    public boolean getShowJoinLeaveMessage() {
        return showJoinLeaveMessage;
    }

    public void setShowJoinLeaveMessage(boolean showJoinLeaveMessage) {
        this.showJoinLeaveMessage = showJoinLeaveMessage;
    }

    public String getJoinLeaveTextChannelID() {
        return joinLeaveTextChannelID;
    }

    public void setJoinLeaveTextChannelID(String joinLeaveTextChannelID) {
        this.joinLeaveTextChannelID = joinLeaveTextChannelID;
    }

    private String botToken;
    private String commandPrefix;
    private String leaveMessage;
    private String joinMessage;
    private boolean showJoinLeaveMessage = false;
    private String joinLeaveTextChannelID;

    public BotSettings(String token){
        botToken = token;
        commandPrefix = "!";
    }

    public BotSettings(){
        commandPrefix = "!";
    }
}
