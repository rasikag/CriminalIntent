package com.rasikagayan.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Rasika Gayan on 12/3/2015.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;

    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public String toString() {
        return getmTitle();
    }
}
