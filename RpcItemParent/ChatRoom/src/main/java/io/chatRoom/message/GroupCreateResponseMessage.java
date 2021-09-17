package io.chatRoom.message;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupCreateResponseMessage  extends AbstractResponseMessage{
    public GroupCreateResponseMessage() {
    }

    public GroupCreateResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupCreateResponseMessage;
    }
}
