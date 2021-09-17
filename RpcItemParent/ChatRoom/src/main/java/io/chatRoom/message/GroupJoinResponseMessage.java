package io.chatRoom.message;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class GroupJoinResponseMessage extends AbstractResponseMessage{

    public GroupJoinResponseMessage() {
    }

    public GroupJoinResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return 0;
    }
}
