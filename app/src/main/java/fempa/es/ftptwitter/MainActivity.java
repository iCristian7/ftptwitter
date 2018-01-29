package fempa.es.ftptwitter;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {

    private static FTPClient cliente = null;
    private Integer puntosLocal;
    private Integer puntosFtp;
    private OutputStream output = null;
    private InputStream in = null;
    private EditText puntos;
    private boolean b;
    private boolean a;
    private EditText usuario;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puntos = (EditText) findViewById(R.id.puntuacion);
        usuario = (EditText) findViewById(R.id.usuario);
        send = (Button) findViewById(R.id.button);


    }

    public void comprobarPuntuacion(final View v) {

        send.setEnabled(false);

        new Thread(new Runnable() {

            @Override
            public void run() {

                String usuarioFTP = "u354500985.fempaalumnos";
                String passFTP = "AlumnosFempa";

                try {
                    cliente = new FTPClient();
                    cliente.setConnectTimeout(10 * 1000);
                    cliente.connect(InetAddress.getByName("31.170.165.160"));
                    boolean login = cliente.login(usuarioFTP, passFTP);
                    if (login) {
                        Log.e("login", "Logeado");
                        Log.e("directorio", cliente.printWorkingDirectory());

                    } else {
                        Log.e("login", "no Logeado");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    String directorio = "Puntuations";
                    ChangeDirectoryCreateIfNotExists(cliente, directorio);
                    Log.e("directorio", cliente.printWorkingDirectory());


                    output = new OutputStream()

                    {
                        private StringBuilder string = new StringBuilder();

                        @Override
                        public void write(int b) throws IOException {
                            this.string.append((char) b);
                        }

                        //Netbeans IDE automatically overrides this toString()
                        public String toString() {
                            return this.string.toString();
                        }
                    };

                    cliente.retrieveFile("TheBest.txt", output);

                     puntosFtp = Integer.parseInt(output.toString());
                     puntosLocal = Integer.parseInt(String.valueOf(puntos.getText()));
                    //texto.setText(puntosLocal+" , "+puntosFtp);
/*
                    if(puntosLocal>999999999){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Por favor introduce un número más pequeño", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
*/
                    if (puntosFtp < puntosLocal) {


                        InputStream in = new ByteArrayInputStream( String.valueOf(puntosLocal).getBytes() );

                        try {
                            cliente.storeFile("TheBest.txt", in);
                        }catch (IOException e){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Por favor introduce un número correcto", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        configuracionTwitter(v);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Enhorabuena, has conseguido la puntuación más alta! ;)", Toast.LENGTH_SHORT).show();

                            }
                        });

                        in.close();
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "JAJAJJAJA Menudo paquete, sigue jugando!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }



                } catch (MalformedURLException e) {
                    Log.w("", "MALFORMED URL EXCEPTION");
                } catch (IOException e) {
                    Log.w(e.getMessage(), e);
                }

                Log.e("puntos", String.valueOf(output));
                Log.e("puntos2", String.valueOf(puntosLocal+"  "+puntosFtp));

                try {
                    cliente.disconnect();
                    //cliente.logout();
                   // in.close();
                    output.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                send.setEnabled(true);
            }

        }).start();


        send.setEnabled(true);

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

    public void configuracionTwitter(View v){

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("AaQE5hN3wD620RKQwHueTcyIX")
                        .setOAuthConsumerSecret("UTpAwKBfXnmKtZan51Ouhj0ff7cyefH5mHSKXGu0Zzei7qFZh5")
                        .setOAuthAccessToken("957267757019656192-4xeh9wk1INdFYkLyhktktPjrFwG04Zw")
                        .setOAuthAccessTokenSecret("aQVbRH7TPkRjToxDTiLG7Hey3Xcnr9cct45cgWc3zphQS");
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();
                Status status = null;
                try {
                    status = twitter.updateStatus("@"+usuario.getText().toString()+" Ha conseguido la nueva puntuación máxima "+puntosLocal+" puntos!!");
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                System.out.println("Successfully updated the status to [" + status.getText() + "].");
            }


}
