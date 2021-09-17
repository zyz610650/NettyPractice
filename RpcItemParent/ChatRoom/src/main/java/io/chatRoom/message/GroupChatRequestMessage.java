package io.chatRoom.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Data
@ToString(callSuper = true)
public class GroupChatRequestMessage extends Message{
    private String groupName;
    private String userName;
    private String content;
    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
