package com.example.floorcountdown.FloorCountDownLib;

import java.util.Observer;

/**
 * Created by air on 2019/3/14.
 */
public interface ICountDownCenter {
    void addObserver(Observer observer);
    void deleteObservers();
    void startCountdown();
    void stopCountdown();
    boolean containHolder(Observer observer);
    void notifyAdapter();
}
