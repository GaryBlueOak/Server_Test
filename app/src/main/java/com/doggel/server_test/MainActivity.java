package com.doggel.server_test;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity {
    private EditText username,password;
    private Button enter;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        enter = (Button)findViewById(R.id.enter);
        status = (TextView)findViewById(R.id.status);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString() + "";
                String pass = username.getText().toString() + "";
                //String stat = "LOGIN STATUS: "; //concat to this for status

                onEnterLogin(name,pass);
            }
        });

        setContentView(R.layout.activity_main);
    }

    private void onEnterLogin(String name, String pass) {
        //signInBackground likely to change
        //everything else is "functional" or at least in the right form.
        new SignInBackground(this,status,0).execute(name,pass);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SignInBackground extends AsyncTask<String, Void, String>{

        private Context context;
        private int operationFlag; // 0 for get, 1 for post
        private TextView tv;

        public SignInBackground(Context context,TextView tv, int flag){
            this.context = context;
            operationFlag = flag;
            this.tv = tv;
        }
        @Override
        protected String doInBackground(String... params) {
            switch(operationFlag){
                case 0:
                    try{
                        String username = (String)params[0];
                        String password = (String)params[1];
                        String link = ""; //link to mysql web serv
                        // "http://myphpmysqlweb.hostei.com/login.php?username=" +username+"&password="+password
                        URL url = new URL(link);
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet();
                        request.setURI(new URI(link));
                        HttpResponse response = client.execute(request);
                        BufferedReader in = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

                        StringBuffer buff = new StringBuffer("");
                        String line ="";
                        while((line = in.readLine())!=null){
                            buff.append(line);
                            //break?
                        }
                        in.close();
                        return buff.toString();
                    }catch(Exception e){

                        return new String("Exception: " + e.getMessage());
                    }

                case 1:
                    try{
                        String username = (String)params[0];
                        String password = (String)params[1];
                        String link="http://myphpmysqlweb.hostei.com/loginpost.php";
                        String data  = URLEncoder.encode("username", "UTF-8")
                                + "=" + URLEncoder.encode(username, "UTF-8");
                        data += "&" + URLEncoder.encode("password", "UTF-8")
                                + "=" + URLEncoder.encode(password, "UTF-8");
                        URL url = new URL(link);
                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter
                                (conn.getOutputStream());
                        wr.write( data );
                        wr.flush();
                        BufferedReader reader = new BufferedReader
                                (new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        // Read Server Response
                        while((line = reader.readLine()) != null)
                        {
                            sb.append(line);
                            break;
                        }
                        return sb.toString();

                    }catch(Exception e){
                        return new String("Exception: " + e.getMessage());
                    }

                default:
                    return new String("Error: Flag Operation not Recognized");
            }




        }
        @Override
         protected void onPostExecute(String result){
            this.tv.setText("LOGIN STATUS: Successful");
        }
    }
}
