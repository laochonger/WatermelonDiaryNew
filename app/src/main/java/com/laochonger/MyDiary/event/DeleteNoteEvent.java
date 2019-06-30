package com.laochonger.MyDiary.event;

public class DeleteNoteEvent {
    private int position;

    public DeleteNoteEvent(int position) {
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
