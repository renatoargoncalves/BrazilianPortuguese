package com.sow.brazilianportuguese.free.listeners;

public class SpeechActivityDetected {

    private OnSpeechEventDetected mOnSpeechEventDetected;

    public void setOnEventListener(OnSpeechEventDetected listener) {
        this.mOnSpeechEventDetected = listener;
    }

    public void doEvent(String event) {
        if (mOnSpeechEventDetected != null)
            mOnSpeechEventDetected.onEvent(event); // event object :)
    }
}