package io.chatRoom.message;

import lombok.Getter;
import lombok.ToString;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Getter
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

   private String interfaceName;
    private Class<?> returnType;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameterValue;
    public RpcRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?> returnType, Class[] parameterTypes, Object[] parameterValue) {
        super.setSequenceId(sequenceId);
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }


    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_REQUEST;
    }
}
