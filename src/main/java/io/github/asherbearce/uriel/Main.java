package io.github.asherbearce.uriel;

import io.github.asherbearce.uriel.commands.Command;
import io.github.asherbearce.uriel.models.UserModel;
import io.github.asherbearce.uriel.settings.BotSettings;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static io.github.asherbearce.uriel.commands.CommandList.*;
//TODO revise mechanism for anti-spam

public class Main {
    public static final String FILE_NAME = "bot.xml";
    public static BotSettings settings;
    private static TextChannel leaveJoinNotificationChannel;
    private static JDA jda;
    private static final int MESSAGE_FREQUENCY_LIMIT = 20;
    private static Map<Long, Integer> messageFrequencyTracker;

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

        //Eventually we want to populate the messageTracker from the database
        messageFrequencyTracker = new HashMap<>();

        for (Member user : jda.getGuilds().get(0).getMembers()){
            messageFrequencyTracker.put(user.getIdLong(), 0);
        }

        (new Thread(() -> {
            //TODO FIX THIS
            while (true) {
                try {
                    Thread.sleep(60000);
                    messageFrequencyTracker.replaceAll((k, v)-> v = 0);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        })).start();

        jda.addEventListener(new EventHandler());
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
                System.out.println(e.getMessage());
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

    public static class EventHandler extends ListenerAdapter{
        public void onGuildMessageReceived(GuildMessageReceivedEvent event){
            //Parse commands here
            if (event.getAuthor().isBot()){
                return;
            }
            String prefix = settings.getCommandPrefix();
            String raw = event.getMessage().getContentRaw();
            if (raw.startsWith(prefix)) {
                List<String> allMatches = new LinkedList<>();
                Matcher m = Pattern.compile("\".*\"|\\S+").matcher(raw);

                while (m.find()) {
                    allMatches.add(m.group().replaceAll("\"", ""));
                }
                String command = allMatches.remove(0);

                String[] args = allMatches.toArray(new String[]{});

                if (command.startsWith(prefix)) {
                    for (Command c : Commands) {

                        if ((prefix + c.getCommandName()).equalsIgnoreCase(command)) {
                            c.Execute(jda, event, args);
                            break;
                        }
                    }
                } else {
                    //Send an error message
                }
            }
            else {
                //Track message frequency
                long authID = event.getAuthor().getIdLong();
                int currentFrequency = messageFrequencyTracker.get(authID) + 1;
                messageFrequencyTracker.replace(authID, currentFrequency);

                if (currentFrequency >= MESSAGE_FREQUENCY_LIMIT){
                    event.getChannel().sendMessage("Hey! <@" + authID + "> Stop spamming! This is a warning. Next time you will be muted!").queue();
                    messageFrequencyTracker.replace(authID, 0);
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
