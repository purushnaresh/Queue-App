package com.kaydarinapp.queueappv2;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kaydarin on 6/1/2016.
 */

//Our class extending fragment
public class Tab3 extends ListFragment {
    // URL to get contacts JSON
    private static String url = "https://raw.githubusercontent.com/mobilesiri/JSON-Parsing-in-Android/master/index.html";

    // JSON Node names
    private static final String TAG_STUDENTINFO = "studentsinfo";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        /*View view = inflater.inflate(R.layout.tab3, container, false);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_listview, mobileArray);

        ListView listView = (ListView) view.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        return view;*/
        new GetStudents().execute();
        return inflater.inflate(R.layout.tab3, container, false);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);

            studentList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), studentList,
                    R.layout.item_listview, new String[]{TAG_NAME, TAG_EMAIL,
                    TAG_PHONE_MOBILE}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            //setListAdapter(adapter); // UNKNOWN ERROR HERE
        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray students = jsonObj.getJSONArray(TAG_STUDENTINFO);

                // looping through All Students
                for (int i = 0; i < students.length(); i++) {
                    JSONObject c = students.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String email = c.getString(TAG_EMAIL);
                    String address = c.getString(TAG_ADDRESS);
                    String gender = c.getString(TAG_GENDER);

                    // Phone node is JSON Object
                    JSONObject phone = c.getJSONObject(TAG_PHONE);
                    String mobile = phone.getString(TAG_PHONE_MOBILE);
                    String home = phone.getString(TAG_PHONE_HOME);

                    // tmp hashmap for single student
                    HashMap<String, String> student = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    student.put(TAG_ID, id);
                    student.put(TAG_NAME, name);
                    student.put(TAG_EMAIL, email);
                    student.put(TAG_PHONE_MOBILE, mobile);

                    // adding student to students list
                    studentList.add(student);
                }
                return studentList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }
}
