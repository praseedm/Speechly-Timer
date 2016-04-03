package praseed.master.speechly;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
,DialogInterface.OnClickListener{
    private TextView timer ;
    private Handler handler;
    private ToggleButton toggleButton;
    private EditText user_input;
    private String input;
    private MyTimer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        timer = (TextView) findViewById(R.id.textview);
        AssetManager assetManager = getAssets();
        Typeface myfont = Typeface.createFromAsset(assetManager, "fonts/SourceSansPro_Light.ttf");
        timer.setTypeface(myfont);
        toggleButton.setOnCheckedChangeListener(this);

        handler = new Handler();
        myTimer = new MyTimer(handler) {
            @Override
            public void onTimerStopped() {
                toggleButton.setChecked(false);
                playSound();
//                timer.setText("00:00");
            }

            @Override
            public void updateUI(long timeRemaining) {
                 timer.setText(MyTimer.cnvrtToString(timeRemaining));
            }
        };

    }

  private void playSound() {
        try {
            AssetFileDescriptor asfd = getAssets().openFd("sounds/tone.mp3");
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(asfd.getFileDescriptor());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int Id = item.getItemId();
        if(Id == R.id.exit){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
//            Toast.makeText(this,"ON",Toast.LENGTH_SHORT).show();
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.user_input, null);
            user_input = (EditText) view.findViewById(R.id.user_input);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Enter the TIme");
            builder.setView(view);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", this);
            builder.setNegativeButton("Cancel",this);
            builder.show();
        }else {
//            Toast.makeText(this,"OFF",Toast.LENGTH_SHORT).show();
            myTimer.stop();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
//                Toast.makeText(this,"Clicked OK",Toast.LENGTH_SHORT).show();
                input = user_input.getText().toString();
                if(MyTimer.isValiedInput(input)){
                            input=input.trim();
                            myTimer.setTimeRemaining(MyTimer.cnvrtToMillisecond(input));
                            myTimer.start();
                }
                else {
                    Toast.makeText(this,"ENTER TIME",Toast.LENGTH_SHORT).show();
                    toggleButton.setChecked(false);
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this,"CANCELLED",Toast.LENGTH_SHORT).show();
                toggleButton.setChecked(false);
                break;
        }
    }
}
