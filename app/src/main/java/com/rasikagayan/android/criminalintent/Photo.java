package com.rasikagayan.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rasika Gayan on 12/11/2015.
 */
public class Photo {

    private static final String JSON_FILENAME = "filename";

    private String mFileName;

    public Photo(String mFileName) {
        this.mFileName = mFileName;
    }

    public Photo(JSONObject json) throws JSONException{
        mFileName = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME,mFileName);
        return json;
    }

    public String getFileName(){
        return mFileName;
    }
}
