package ru.badboy.infometer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Евгений on 04.06.2016.
 */
public class HistoryItem {

    private static final String JSON_QUESTION = "question";
    private static final String JSON_ANSWER = "answer";

    private String mAnswer;
    private String mQuestion;

    public HistoryItem(String mQuestion, String mAnswer) {
        this.mQuestion = mQuestion;
        this.mAnswer = mAnswer;
    }

    public HistoryItem(JSONObject json) throws JSONException {
        mQuestion = json.getString(JSON_QUESTION);
        mAnswer = json.getString(JSON_ANSWER);
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_QUESTION, mQuestion);
        json.put(JSON_ANSWER, mAnswer);
        return json;
    }
}
