package io.chatRoom.message;

import io.chatRoom.Service.Session.Group;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends Message{
    private String name;
    private Set<String> members;

    public GroupCreateRequestMessage(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }



    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
