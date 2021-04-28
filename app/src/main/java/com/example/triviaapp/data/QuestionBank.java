package com.example.triviaapp.data;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviaapp.R;
import com.example.triviaapp.controller.MySingleton;
import com.example.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private RequestQueue mRequestQueue;
    ArrayList<Question> mQuestionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    private Context mContext;

    public  QuestionBank(Context mContext){
        this.mContext = mContext;
    }
    public List<Question> getQuestion(final AnswerListAsyncResponse callBack){
        mRequestQueue = MySingleton.getInstance(mContext).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length();i++){
                            try {
                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).getString(0));
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                                //Log.d("JSON",i+" onResponse: "+response.getJSONArray(i).getString(0));
                                //Log.d("JSON",i+" onResponse: "+response.getJSONArray(i).getBoolean(1));
                                mQuestionArrayList.add(question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != callBack){
                            callBack.processFinished(mQuestionArrayList);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jsonArrayRequest);
        return mQuestionArrayList;
    }
}
