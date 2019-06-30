package com.laochonger.MyDiary.event;


public class StartUpdateDiaryEvent {

    private int position;

    public StartUpdateDiaryEvent(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}