package com.example.zhangweilong.zwl;

/**
 * Created by zhangweilong on 15/12/3.
 *
 * 用GsonFormat
 */
public class JsonBean {

    /**
     * userName : zhangsan
     * age : 14
     * sex : true
     */

    private String userName;
    private int age;
    private boolean sex;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }

    public boolean isSex() {
        return sex;
    }
}
