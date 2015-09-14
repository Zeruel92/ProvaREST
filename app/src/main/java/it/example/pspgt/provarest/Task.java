package it.example.pspgt.provarest;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by gabry on 14/09/15.
 */
public class Task extends AsyncTask<Integer,Integer,String>{

    @Override
    protected String doInBackground(Integer... params) {
        publishProgress(0);
        String result="Problemi di connessione";
        String url="http://ec2-52-17-122-110.eu-west-1.compute.amazonaws.com/index.php/Opera/0";
        HttpClient request=new DefaultHttpClient();
        HttpGet get=new HttpGet(url);
        HttpResponse response= null;
        try {
            response = request.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int responseCode=response.getStatusLine().getStatusCode();
        publishProgress(20);
        if(responseCode==200){
            result="";
            try {
                InputStream istream = response.getEntity().getContent();
                BufferedReader r = new BufferedReader(new InputStreamReader(istream));
                String s = null;
                StringBuffer sb = new StringBuffer();
                while ((s = r.readLine()) != null) {
                    sb.append(s);
                }
                publishProgress(50);
                JSONArray array = new JSONArray(sb.toString());

                for (int i = 0;i < array.length() ;i++){
                    String nome = array.getJSONObject(i).getString( "Nome");
                    String cognome = array.getJSONObject(i).getString("Descrizione");
                    result+="Nome "+nome+" Descrizione "+cognome+"\n";
                }
                publishProgress(70);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        publishProgress(100);
        return result;
    }

    @Override
    protected void onPostExecute(String s) {

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
