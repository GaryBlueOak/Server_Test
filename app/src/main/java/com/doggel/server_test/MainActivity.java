package com.doggel.server_test;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    JSONObject jobj = null;
    ServerSide clientServerInterface = new ServerSide();
    TextView textView;
    String ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.status);
        //start background processing
        new RetreiveData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    class RetreiveData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... arg0) {
            // where we will put our mysql url
            jobj = clientServerInterface.makeHttpRequest("http://www.yourwebsite.com/at/serverside.php");

            try {
                ab = jobj.getString("key");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return ab;
        }
        protected void onPostExecute(String ab){

            textView.setText(ab);
        }

}

}
/* This is the PHP that should be added.
<?php
        $con = mysql_connect("localhost","username","password");
        mysql_select_db("database_name",$con);
        $result = mysql_query("SELECT * FROM pizza_table");
        $arr = array();
        echo json_encode($arr);
        ?>
*/
