package io.github.asherbearce.uriel;

import io.github.asherbearce.uriel.commands.Command;
import io.github.asherbearce.uriel.commands.Mute;
import io.github.asherbearce.uriel.commands.Warn;
import io.github.asherbearce.uriel.database.Database;
import io.github.asherbearce.uriel.models.UserModel;
import io.github.asherbearce.uriel.settings.BotSettings;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static io.github.asherbearce.uriel.commands.CommandList.*;
//TODO revise mechanism for anti-spam
//TODO revise warning messages to use string formatting instead of concatenation

public class Main {
    public static final String FILE_NAME = "bot.xml";
    public static BotSettings settings;
    private static TextChannel leaveJoinNotificationChannel;
    private static JDA jda;
    private static final int MESSAGE_FREQUENCY_LIMIT = 4;
    private static Map<Long, UserModel> users;
    public static boolean isUnderLockdown = false;
    public static Database db;

    public static JDA getJda(){
        return jda;
    }

    public static void main(String[] args) throws Exception{
        File tokenFile = new File(FILE_NAME);

        if (tokenFile.exists()){
            try{

                XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_NAME)));
                settings = (BotSettings) decoder.readObject();
                if (settings.getShowJoinLeaveMessage()) {
                    leaveJoinNotificationChannel = jda.getTextChannelById(settings.getJoinLeaveTextChannelID());
                }
                decoder.close();
                jda = new JDABuilder(AccountType.BOT).setToken(settings.getBotToken()).build();
                Thread.sleep(1000);
            } catch (Exception e){
                System.out.println("There was a problem retrieving bot settings. Performing first time set up.");
                firstTimeSetup();
            }
        } else {
            Thread.sleep(1000);
            firstTimeSetup();
        }
        jda.getPresence().setActivity(Activity.watching("Type " + Main.settings.getCommandPrefix() + "Help for help!"));
        //Eventually we want to populate the messageTracker from the database
        users = new HashMap<>();

        while (jda.getGuilds().isEmpty()){
            //Keep trying to retrieve the users
        }

        for (Member user : jda.getGuilds().get(0).getMembers()){
            UserModel model = new UserModel();
            model.userID = user.getIdLong();
            model.spamWarnings = 0;
            users.put(user.getIdLong(), model);
        }

        (new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    users.replaceAll((k, v)-> {v.currentMessageFrequency = 0; return v;});
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        })).start();

        jda.addEventListener(new EventHandler());
        settings.registerEventHandler(new PrefixChangedEventHandler());

        //Create our database
        try {
            db = Database.getDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){

        }
    }

    private static void firstTimeSetup(){
        boolean loginSuccess = false;

        while (!loginSuccess){
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter the valid token of the bot: ");
            String s = input.nextLine();

            try{
                jda = new JDABuilder(AccountType.BOT).setToken(s).build();
                loginSuccess = true;

                settings = new BotSettings(s);
                if (settings.getShowJoinLeaveMessage()) {
                    leaveJoinNotificationChannel = jda.getTextChannelById(settings.getJoinLeaveTextChannelID());
                }
                XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_NAME)));
                encoder.writeObject(settings);
                encoder.close();
            }
            catch (LoginException e){
                e.printStackTrace();
                System.out.println("An error occurred while logging in. It may be that the token entered was invalid.");
            }
            catch (IOException e){
                System.out.println("There was an error storing your bots token. Please retry.");
            }
        }
    }

    public static void UpdateSettings(){
        try{
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_NAME)));
            encoder.writeObject(settings);
            encoder.close();
        }
        catch (IOException e){
            //Send an error message here
        }
    }

    public static void sendErrorMessage(String errorMessage, TextChannel channel){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("An error has occurred!");
        embedBuilder.setDescription(errorMessage);

        channel.sendMessage(embedBuilder.build()).queue();
    }

    public static class PrefixChangedEventHandler implements BotSettings.SettingsChangedEventHandler{
        @Override
        public void onCommandPrefixChanged(){
            jda.getPresence().setActivity(Activity.watching("Type " + Main.settings.getCommandPrefix() + "Help for help!"));
        }
    }

    public static class EventHandler extends ListenerAdapter{
        public void onGuildMessageReceived(GuildMessageReceivedEvent event){
            String raw = event.getMessage().getContentRaw();
            String[] split = raw.split("[.,\\/#!$%\\^&\\*;:{}=\\-_`~()]\\S*");

            for (int i = 0; i < split.length; i++){
                for (String badword : settings.getBlacklistWords()) {
                    if (split[i].equalsIgnoreCase(badword)){

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("Inappropriate words have been filtered");
                        embedBuilder.setDescription("Don't worry, this isn't a warning!");
                        embedBuilder.addField("Offensive item blocked: ", event.getMessage().getContentRaw(), false);
                        event.getMember().getUser().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();
                        event.getChannel().deleteMessageById(event.getMessageId()).complete();
                        break;
                    }
                }
            }

            if (event.getAuthor().isBot()){
                return;
            }

            String prefix = settings.getCommandPrefix();

            if (raw.startsWith(prefix)) {
                List<String> allMatches = new LinkedList<>();
                Matcher m = Pattern.compile("\".*\"|\\S+").matcher(raw);

                while (m.find()) {
                    allMatches.add(m.group().replaceAll("\"", ""));
                }
                String command = allMatches.remove(0);

                String[] args = allMatches.toArray(new String[]{});

                if (command.startsWith(prefix)) {
                    Command c = getCommandByName(command.substring(1));

                    if (c.getMinArguments() <= args.length && c.getMaxArguments() >= args.length) {
                        String result = c.Execute(jda, event, args);

                        if (settings.getLogChannelID() != null && !result.contentEquals("NoLog")){
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle("Command Logged");
                            embedBuilder.addField("Command used:", c.getCommandName(), false);
                            embedBuilder.addField("Invoker:", event.getMember().getEffectiveName(), false);
                            embedBuilder.addField("Date called:", (new Date()).toString(), false);
                            embedBuilder.addField("Callback: ", result, false);

                            event.getGuild().getTextChannelById(settings.getLogChannelID()).sendMessage(embedBuilder.build()).queue();
                        }
                    }
                    else {
                        sendErrorMessage("Invalid number of arguments: " + args.length +
                                ", this command takes at most " + c.getMaxArguments() + ", and at least " +
                                c.getMinArguments() + " arguments. See the help command for assistance.", event.getChannel());
                    }
                }
            }
            else {
                long authID = event.getAuthor().getIdLong();
                UserModel user = users.get(authID);
                user.currentMessageFrequency += 1;
                users.replace(authID, user);

                if (user.currentMessageFrequency >= MESSAGE_FREQUENCY_LIMIT){
                    user.spamWarnings += 1;
                    user.currentMessageFrequency = 0;

                    users.replace(authID, user);

                    if (user.spamWarnings > 1){
                        Mute.muteUser(event.getMember(), event.getGuild(), 0, 10);
                        user.spamWarnings = 0;
                        Warn.giveUserWarn(event.getMember(), event.getGuild(), new Date(), jda.getSelfUser().getId(), "Spamming in " + event.getChannel().getName());
                        event.getChannel().sendMessage("A warning has been issued to <@" + authID + ">, and has been muted for 10 minutes for spamming.").queue();
                    }
                    else {
                        event.getChannel().sendMessage("Hey! <@" + authID + "> Stop spamming! This is a warning. Next time you will be muted!").queue();
                    }
                }
            }
        }

        public void onGuildMemberLeave(GuildMemberLeaveEvent event){
            if (settings.getShowJoinLeaveMessage()) {
                leaveJoinNotificationChannel.sendMessage(event.getUser().getName() + settings.getLeaveMessage()).queue();
            }
        }

        public void onGuildMemberJoin(GuildMemberJoinEvent event){
            if (settings.getShowJoinLeaveMessage()) {
                leaveJoinNotificationChannel.sendMessage(event.getUser().getName() + settings.getJoinMessage()).queue();
            }
        }
    }
}
