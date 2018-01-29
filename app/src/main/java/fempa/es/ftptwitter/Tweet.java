package fempa.es.ftptwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by daniel on 22/01/2018.
 */

public class Tweet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtTitulo = (TextView) findViewById(R.id.textView4);
    }


    public void configuracionTwitter() throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("AaQE5hN3wD620RKQwHueTcyIX")
                .setOAuthConsumerSecret("UTpAwKBfXnmKtZan51Ouhj0ff7cyefH5mHSKXGu0Zzei7qFZh5\n")
                .setOAuthAccessToken("957267757019656192-4xeh9wk1INdFYkLyhktktPjrFwG04Zw")
                .setOAuthAccessTokenSecret("\taQVbRH7TPkRjToxDTiLG7Hey3Xcnr9cct45cgWc3zphQS\n");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Status status = twitter.updateStatus("Aqui va el mensaje :3");
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
    }

}



