package io.github.asherbearce.uriel.commands;

import io.github.asherbearce.uriel.Main;
import io.github.asherbearce.uriel.settings.BotSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

//TODO this needs a revamp
public class Settings implements Command{
    private List<Setting> settings = new LinkedList<>();

    @Override
    public String getCommandName() {
        return "Settings";
    }

    @Override
    public String getDescription() {
        return "Allows a user to change general settings for this bot.";
    }

    @Override
    public String getArgumentList() {
        return "```prefix``` **Settings** ```[Setting Name]```(use ```prefix```**Settings** for a list of settings)";
    }

    @Override
    public int getMaxArguments() {
        return 2;
    }

    @Override
    public int getMinArguments() {
        return 0;
    }

    @Override
    public String Execute(JDA jda, Guild guild, TextChannel channel, Member author, String[] args) {

        String returnValue = "NoLog";

        if (args.length == 0){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Settings list");

            for (Setting s : settings){
                builder.addField(s.setting.getName(), "", false);
                System.out.println(s.setter.getName());
            }

            channel.sendMessage(builder.build()).queue();
        } else {

        }

        return returnValue;
    }

    public Settings(){
        Field[] allFields = BotSettings.class.getDeclaredFields();
        for (Field field : allFields){

            if (Modifier.isPrivate(field.getModifiers())){
                for (Method method : BotSettings.class.getDeclaredMethods()){
                    if (isSetter(field.getName().toLowerCase(), method)){
                        settings.add(new Setting(field, method));
                        break;
                    }
                }
            }
        }
    }

    private static boolean isSetter(String fieldName, Method method){
        

        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                (method.getName().toLowerCase().matches("^set" + fieldName + ".*") ||
                        method.getName().toLowerCase().matches("^add" + fieldName + ".*"));
    }

    static class Setting{
        public Field setting;
        public Method setter;

        public Setting(Field setting, Method setter){
            this.setting = setting;
            this.setter = setter;
        }
    }
}
