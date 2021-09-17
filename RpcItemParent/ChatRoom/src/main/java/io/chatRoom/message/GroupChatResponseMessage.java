package io.chatRoom.message;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupChatResponseMessage extends AbstractResponseMessage{
    private String from;
    private String content;

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }
    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
