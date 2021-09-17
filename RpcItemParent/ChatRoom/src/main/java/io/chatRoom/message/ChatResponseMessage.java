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
public class ChatResponseMessage extends AbstractResponseMessage{


    private String from;
    private String content;
    public ChatResponseMessage() {
    }

    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }


    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }

}
