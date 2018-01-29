package fempa.es.ftptwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private  static FTPClient cliente = null;
    private static TextView texto;
    private int puntuacionLocal=0;
    private   OutputStream output=null;
    private boolean b;
    private boolean a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText puntos=(EditText)findViewById(R.id.puntuacion);
        EditText usuario=(EditText)findViewById(R.id.usuario);
        texto=(TextView)findViewById(R.id.texto);



    }

    public void comprobarPuntuacion(View v)  {


        new Thread(new Runnable() {

            @Override
            public void run() {

                String usuarioFTP = "u354500985.fempaalumnos";
                String passFTP = "AlumnosFempa";

                try {
                    cliente = new FTPClient();
                    cliente.setConnectTimeout(10 * 1000);
                    cliente.connect(InetAddress.getByName("31.170.165.160"));
                    boolean login = cliente.login(usuarioFTP,passFTP);
                    if(login){
                        Log.e("login","Logeado");
                        Log.e("directorio",cliente.printWorkingDirectory());

                    }else{
                        Log.e("login","no Logeado");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

               try {

                   String directorio="Puntuations";
                   ChangeDirectoryCreateIfNotExists(cliente,directorio);
                   Log.e("directorio",cliente.printWorkingDirectory());



                   output = new OutputStream()

                    {
                        private StringBuilder string = new StringBuilder();
                        @Override
                        public void write(int b) throws IOException {
                            this.string.append((char) b );
                        }

                        //Netbeans IDE automatically overrides this toString()
                        public String toString(){
                            return this.string.toString();
                        }
                    };

                   cliente.retrieveFile("TheBest.txt", output);

                } catch (MalformedURLException e) {
                    Log.w("", "MALFORMED URL EXCEPTION");
                } catch (IOException e) {
                    Log.w(e.getMessage(), e);
                }

                Log.e("puntos", output.toString());

                try {
                    cliente.disconnect();
                    //cliente.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();


    }

    public boolean ChangeDirectoryCreateIfNotExists(FTPClient mFtpClient, String directory) {
        try {
            if (mFtpClient.changeWorkingDirectory(directory)) {
                return true;
            } else {
                mFtpClient.makeDirectory(directory);
                return mFtpClient.changeWorkingDirectory(directory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TestFTP", "Algo fue mal :o");
        }

        return true;
    }

}
