package com.lcs.eventbus;

public class EventBean {
    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public EventBean(String one, String two) {
        this.one = one;
        this.two = two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    private String one;

    private String two;

    @Override
    public String toString() {
        return "EventBean{" +
                "one='" + one + '\'' +
                ", two='" + two + '\'' +
                '}';
    }
}
