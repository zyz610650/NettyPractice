package io.chatRoom.Service.Session;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Data
public class Group {

    private String name;
    private Set<String> members;

    public static final  Group EMPTY_GROUP=new Group("empty", Collections.emptySet());
    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}
