package io.chatRoom.message;

import lombok.Data;
import lombok.Getter;
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
public class RpcResponseMessage extends AbstractResponseMessage {

private Object returnValue;
private Exception exceptionValue;

    public RpcResponseMessage(Object returnValue) {
        this.returnValue = returnValue;
    }

    public RpcResponseMessage(boolean success, String reason, Object returnValue) {
        super(success, reason);
        this.returnValue = returnValue;
    }

    public RpcResponseMessage() {

    }

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
