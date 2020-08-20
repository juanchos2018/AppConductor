package com.conductor.appconductor.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conductor.appconductor.R;

public class HilosActivity extends AppCompatActivity {

    Button btnSinHilos;

    ProgressDialog pDialog;
    Button btn,btn2;
    boolean ok=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilos);

        btn=(Button)findViewById(R.id.btnejecutar);
        btn2=(Button)findViewById(R.id.btnparar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time time = new time();
                time.execute();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time time = new time();
            // time.onCancelled(true);
                // time.isCancelled();
                ok=false;
                time.onCancelled();
            }

        });

    }

    public  void  Hilo(){
        try {


            Thread.sleep(2000);

        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
       }

        public void  ejecutar(){
          time time= new time();
          time.execute();

        }

    public  class  time extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {

           // if (ok){
                for (int i=0;i<4;i++)
                {
                    Hilo();

                    if (isCancelled()){
                        break;
                    }
                }
           // }

            return  true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
          //  super.onPostExecute(aBoolean);

        //    Toast.makeText(HilosActivity.this, String.valueOf( aBoolean), Toast.LENGTH_LONG).show();
            ejecutar();


            if(ok){
                hola();
            }


          //  Toast.makeText(HilosActivity.this, "cada 3 segundo", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
          //  Toast.makeText(HilosActivity.this, "cancelado", Toast.LENGTH_SHORT).show();
        }
    }

    public  void  hola(){
        Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show();
    }
}
