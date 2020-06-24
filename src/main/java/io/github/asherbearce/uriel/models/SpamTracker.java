package io.github.asherbearce.uriel.models;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.LinkedList;
import java.util.List;

//Criteria for spam: 3 same messages in a row, messages sent in quick succession (5 seconds for 3 messages), mass mentions
public class SpamTracker {
    public List<Message> lastMessages;
    public Member member;
    public boolean warned;
    public static final int MENTION_LIMIT = 5;

    public boolean isSpamming(){
        boolean result = false;

        if (getMostRecentMessage().getMentionedMembers().size() >= MENTION_LIMIT){
            result = true;
        } else if (
                lastMessages.size() == 3 &&
                lastMessages.get(0).getContentRaw().contentEquals(lastMessages.get(1).getContentRaw()) &&
                lastMessages.get(0).getContentRaw().contentEquals(lastMessages.get(2).getContentRaw())){
            result = true;
        } else if (lastMessages.size() == 3 &&
                lastMessages.get(0).getTimeCreated().plusSeconds(5).isAfter(lastMessages.get(2).getTimeCreated())){
            result = true;
        }

        return result;
    }

    public void updateMessages(Message message){
        if (lastMessages.size() == 3){
            lastMessages.remove(0);
        }

        lastMessages.add(message);
    }

    public Message getMostRecentMessage(){
        return lastMessages.get(lastMessages.size() - 1);
    }

    public SpamTracker(Member member){
        this.member = member;
        this.lastMessages = new LinkedList<>();
    }
}
