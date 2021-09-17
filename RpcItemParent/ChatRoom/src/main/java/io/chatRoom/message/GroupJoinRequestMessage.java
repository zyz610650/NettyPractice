package io.chatRoom.message;

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
public class GroupJoinRequestMessage extends Message{
    private String groupName;
    private String userName;
    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}
