package com.hhspls.voicerecognition.models;

/**
 * Created by HICT-HP on 5/20/2017.
 */


import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;

/**
 * Outcome class for deserialization purpose
 * Created by Wit on 5/30/13.
 */

public class WitOutcome {

    @SerializedName("msg_id")
    private String msgId;

    @SerializedName("entities")
    private HashMap<String, JsonElement> entities;

    @SerializedName("_text")
    private String text;

    public HashMap<String, JsonElement> get_entities() {
        return entities;
    }

    public String get_text() {
        return text;
    }

    //  {
    //        "msg_id": "0oMe12qmGJ9hvmQSL",
    //        "entities": {
    //    "cuisine": {
    //        "value": "chinese",
    //        "type": "cuisines",
    //        "body": "from Pekin"
    //        },
    //        "location": {
    //        "value": [
    //        90.785,
    //        45.897
    //        ],
    //        "type": "location",
    //        "body": "close to Montmartre"
    //        }

}
