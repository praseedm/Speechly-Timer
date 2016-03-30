package praseed.master.speechly;

import android.os.Handler;
import android.util.Log;

/**
 * Created by praseedm on 3/30/2016.
 */
public abstract class MyTimer implements Runnable {
    private long timeRemaining;
    private Handler handler;
    private boolean isKilled = false;

    public MyTimer(Handler handler) {
        this.handler = handler;
        this.timeRemaining = 0;
    }

    public MyTimer(Handler handler, long timeRemaining) {
        this.timeRemaining = timeRemaining;
        this.handler = handler;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    //***Helper fns***//
    public static boolean isValiedInput(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        String timmed = input.trim();
        if (timmed.length() == 5 && timmed.indexOf(":") == 2) {
            return true;
        } else {
            return false;
        }
    }


    public static long cnvrtToMillisecond(String timeInput) {
        try {
            int minutes = Integer.parseInt(timeInput.substring(0, 2));
            int seconds = Integer.parseInt(timeInput.substring(3, timeInput.length()));
            long millisecconds = (minutes * 60 + seconds) * 1000;
            return millisecconds;
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public static String cnvrtToString(long timeInput) {
        int totalseconds = (int) (timeInput / 1000);
        int minutes = totalseconds / 60;
        int seconds = totalseconds % 60;
        String minutesString = (minutes < 10) ? "0" + minutes : minutes + "";
        String seccondsString = (seconds < 10) ? "0" + seconds : seconds + "";
        return minutesString+":"+seccondsString;
    }

    //*********//


    public void start() {

        handler.postDelayed(this, 1000);
    }
    public void stop(){
//        isKilled = true;
        timeRemaining = 0;
    }


    @Override
    public void run() {
//     if(!isKilled){
        Log.d("Timer", ":called!!");
        updateUI(timeRemaining);
        timeRemaining -= 1000;
        if (timeRemaining >= 0) {
            handler.postDelayed(this, 1000);
        }
        else {
            onTimerStopped();
        }
//     }
    }

    public abstract void onTimerStopped();

    public abstract void updateUI(long timeRemaining);
}
