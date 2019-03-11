package com.android.personalbest.addFriend;

import java.util.ArrayList;
import java.util.Collection;

public class AddFriend implements IAddFriendSubject<IAddFriendObserver>{

    private final Collection<IAddFriendObserver> observers;

    public AddFriend(){
        observers = new ArrayList<>();
    }

    @Override
    public void register(IAddFriendObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(IAddFriendObserver observer) {

    }

    @Override
    public void addFriendUpdate() {
        for(IAddFriendObserver observer : observers ){
            observer.onAppUserCheckCompleted();
            observer.onIsInUserPendingListCheckCompleted();
            observer.onIsInFriendListCheckCompleted();
            observer.onIsInFriendPendingListCheckCompleted();
        }
    }
}
