package com.lizehao.watermelondiarynew.event;

public class StarLookDiaryEvent {
    private int position;

    public StarLookDiaryEvent(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
