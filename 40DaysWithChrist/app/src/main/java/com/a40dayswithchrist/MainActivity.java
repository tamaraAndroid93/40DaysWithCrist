package com.a40dayswithchrist;


import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static final String EMAIL = "info@40dayswithChrist.com";
    private static final String MY_PREFS_NAME = "pref";


    private Button mBStartNext;
    private Button mBPreviousSes;
    private Button mBContactUs;
    public static Database mDatabase;
    public static DateDatabase mDatabaseDate;
    public static MediaPlayer mediaPlayer;
    private ImageView mIvPlay;
    private ImageView mIvFast;
    private ImageView mIvEnd;
    private ImageView mIvSlow;
    private ImageView mIvBegin;

    private int days;

    private int duration;
    public static String date = "07/07/1977";
    public static int day = 0;


    private String data;

    private boolean playing = false;
    private ArrayList<String> mList;
    public static TextView mIvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        initComponents();
        addListenres();
        Toast.makeText(getApplicationContext(), "The application needs internet conection", Toast.LENGTH_SHORT).show();
        NotificationManager notifManager= (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE); notifManager.cancelAll();

    }

    @Override
    public void onConfigurationChanged(Configuration config) {

        super.onConfigurationChanged(config);
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main);
        }
        initComponents();
        addListenres();

        if (mediaPlayer !=null){
            playing=true;
            mIvPlay.setImageResource(R.drawable.pause);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }


    private void addListenres() {
        mBPreviousSes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PreviousActivity.class));
                mIvPlay.setImageResource(R.drawable.play);
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

            }
        });

        mIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (playing) {

                        mediaPlayer.pause();
                        duration = mediaPlayer.getCurrentPosition();
                        mIvPlay.setImageResource(R.drawable.play);
                        playing = false;
                    } else {
                        //     mediaPlayer = new MediaPlayer();
                        mediaPlayer.seekTo(duration);
                        mediaPlayer.start();
                        mIvPlay.setImageResource(R.drawable.pause);
                        playing = true;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Click on TODAY'S DEVOTIONAL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mIvFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (playing) {
                        mediaPlayer.pause();
                        duration = mediaPlayer.getCurrentPosition() + 1000;
                        mediaPlayer.seekTo(duration);
                        mediaPlayer.start();

                    }
                }
            }
        });

        mIvSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (playing) {
                        mediaPlayer.pause();
                        duration = mediaPlayer.getCurrentPosition() - 1000;
                        mediaPlayer.seekTo(duration);
                        mediaPlayer.start();

                    }
                }
            }
        });

        mIvBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playing) {
                    mediaPlayer.pause();
                    duration = 0;
                    mediaPlayer.seekTo(duration);
                    mediaPlayer.start();

                }
            }

        });

        mIvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playing) {
                    mediaPlayer.pause();
                    duration = mediaPlayer.getDuration();
                    mediaPlayer.seekTo(duration);
                    mediaPlayer.start();
                    mIvPlay.setImageResource(R.drawable.play);
                    playing = false;
                    mediaPlayer = null;
                }
            }

        });

        mBContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        mBStartNext.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                play(mDatabase.getCvList_Id().get(mDatabase.getCvList_Id().size() - 2).getPath());
            }
        });
    }

    private void initComponents() {
        mDatabase = new Database(getApplicationContext());
        mDatabaseDate = new DateDatabase(getApplicationContext());
        mList = mDatabaseDate.getList_Id();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy/MM/dd", Locale.ENGLISH);
        String cDateTime = dateFormat.format(new Date());
        if (mDatabaseDate.getList_Id().size() == 0) {
            mDatabaseDate.insertDate(cDateTime);
            downloadUrl(1);
            Record record = new Record(data);
            mDatabase.insertCv(record);
            downloadUrl(2);
            Record record2 = new Record(data);
            mDatabase.insertCv(record2);

        } else if (mDatabaseDate.getList_Id().size() < 40) {
            if (!mList.get(mList.size() - 1).equalsIgnoreCase(cDateTime)) {

                mDatabaseDate.insertDate(cDateTime);
                downloadUrl(mDatabaseDate.getList_Id().size() + 1);
                Record record = new Record(data);
                mDatabase.insertCv(record);
                days=mDatabaseDate.getList_Id().size()-1;
            }
        }


        mBContactUs = (Button) findViewById(R.id.buttonContactUs);
        mBPreviousSes = (Button) findViewById(R.id.buttonPrevious);
        mBStartNext = (Button) findViewById(R.id.buttonToday);
        mIvPlay = (ImageView) findViewById(R.id.imageViewPlay);
        mIvFast = (ImageView) findViewById(R.id.imageViewFast);
        mIvSlow = (ImageView) findViewById(R.id.imageViewSlow);
        mIvEnd = (ImageView) findViewById(R.id.imageViewNext);
        mIvBegin = (ImageView) findViewById(R.id.imageViewBack);
        mIvTitle = (TextView) findViewById(R.id.textView);

        mIvTitle.setText("Today is day " + mDatabaseDate.getList_Id().size());
        if (mDatabaseDate.getList_Id().size() == 40) {
            mBContactUs.setText("Request certificate");
        }

    }

    protected void sendEmail() {
        Log.i("Send email", "");
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", EMAIL, null));

        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void play(String path) {


        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mIvPlay.setImageResource(R.drawable.pause);

        playing = true;
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(false);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        mediaPlayer.start();


    }

    public void downloadUrl(int number) {
        String url = "http://www.40dayswithchrist.com/uploads/40_Days_-_Day_" + number + ".mp3";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("");
        request.setTitle("");
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();

        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/App2/Christ/40_days_with_christ_-_Day_" + number + ".mp3");
        data = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download//App2/Christ/40_days_with_christ_-_Day_" + number + ".mp3";


// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseDate.closeDataBase();
        mDatabase.closeDataBase();

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=null;

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(),MainActivity.class), 0);
        Notification.Builder notificationBuilder=new Notification.Builder(getApplicationContext()).setTicker(
                "40 Days with Christ").setSmallIcon(R.drawable.play).setAutoCancel(true)
                .setContentText(mIvTitle.getText().toString()).setContentInfo(""+1)
                .setContentTitle("Come back on lecture")
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            n = notificationBuilder.build();
        }

        n.defaults = Notification.FLAG_AUTO_CANCEL;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(1111,notificationBuilder.build());
        }

    }
}





