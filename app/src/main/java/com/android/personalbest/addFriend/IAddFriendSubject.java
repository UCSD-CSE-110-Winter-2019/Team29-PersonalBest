package com.android.personalbest.addFriend;

public interface IAddFriendSubject<ObserverT> {
    /**
     * Register a new listener.
     */
    void register(ObserverT observer);

    /**
     * Unregister a listener.
     */
    void unregister(ObserverT observer);

    void addFriendUpdate();

}
