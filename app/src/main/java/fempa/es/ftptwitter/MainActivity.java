package fempa.es.ftptwitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void comprobarPuntuacion(View v)  {


        new Thread(new Runnable() {

            @Override
            public void run() {
                FTPClient cliente = null;
                String usuarioFTP = "u354500985.fempaalumnos";
                String passFTP = "AlumnosFempa";

                try {
                    cliente = new FTPClient();
                    cliente.setConnectTimeout(10 * 1000);
                    cliente.connect(InetAddress.getByName("31.170.165.160"));
                    boolean login = cliente.login(usuarioFTP,passFTP);
                    if(login){
                        Log.e("login","Logeado");
                        Toast.makeText(MainActivity.this, "Logeado", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("login","no Logeado");
                        Toast.makeText(MainActivity.this, "Error al logearse", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    cliente.disconnect();
                    //cliente.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

}
