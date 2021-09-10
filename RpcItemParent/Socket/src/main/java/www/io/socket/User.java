package www.io.socket;

import java.io.Serializable;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class User implements Serializable {
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
