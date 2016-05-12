package com.mx.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.mx.R;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {


    @BindView(R.id.mVideoView)
    VideoView mVideoView;
    private String url;
    private String shareUrl;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("url");
        shareUrl = getIntent().getStringExtra("shareUrl");
        title = getIntent().getStringExtra("title");
        mVideoView.setVideoPath(url);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressDialog.dismiss();
                Toast.makeText(VideoActivity.this, "视频不存在或者已被删除",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        CustomMediaController customMediaController=new CustomMediaController(this);
        customMediaController.setListener(new OnMediaControllerInteractionListener() {
            @Override
            public void onShareClickListener() {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,title+" "+shareUrl+
                        getString(R.string.share_tail));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,getString(R.string.share)));
            }
        });

        mVideoView.setMediaController(customMediaController);
        mVideoView.start();

    }

    public  interface  OnMediaControllerInteractionListener{
        void onShareClickListener();
    }

    public class CustomMediaController extends MediaController {
        private Context mContext;
        private OnMediaControllerInteractionListener mListener;


        public CustomMediaController(Context context) {
            super(context);
            mContext=context;
        }
        public void setListener(OnMediaControllerInteractionListener listener){
            mListener=listener;
        }

        @Override
        public void setAnchorView(View view) {
            super.setAnchorView(view);
            FrameLayout.LayoutParams frameLayoutParams=new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayoutParams.setMargins(0,50,500,0);
            frameLayoutParams.gravity= Gravity.RIGHT|Gravity.TOP;

            ImageButton imageButton= (ImageButton) LayoutInflater.from(mContext)
            .inflate(R.layout.share_button,null,false);

            imageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onShareClickListener();
                    }
                }
            });
            addView(imageButton,frameLayoutParams);
        }

        @Override
        public void show(int timeout) {
            super.show(timeout);

            int currentapiVersion= Build.VERSION.SDK_INT;
            if(currentapiVersion<Build.VERSION_CODES.JELLY_BEAN_MR2){

                try {
                    Field field1 = MediaController.class.getDeclaredField("mAnchor");
                    field1.setAccessible(true);
                    View mAnchor = (View) field1.get(this);

                    Field field2 = MediaController.class.getDeclaredField("mDecor");
                    field2.setAccessible(true);
                    View mDecor= (View) field2.get(this);

                    Field field3 = MediaController.class.getDeclaredField("mDecorLayoutParams");
                    field3.setAccessible(true);
                    WindowManager.LayoutParams mDecorLayoutParams = (WindowManager.LayoutParams) field3.get(this);

                    Field field4 = MediaController.class.getDeclaredField("mWindowManager");
                    field4.setAccessible(true);
                    WindowManager mWindowManager = (WindowManager)field4.get(this);

                    // NOTE: this appears in its own Window so co-ordinates are screen co-ordinates
                    int [] anchorPos = new int[2];
                    mAnchor.getLocationOnScreen(anchorPos);

                    // we need to know the size of the controller so we can properly position it
                    // within its space
                    mDecor.measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(),
                            MeasureSpec.AT_MOST),
                            MeasureSpec.makeMeasureSpec(mAnchor.getHeight(),
                                    MeasureSpec.AT_MOST));

                    mDecor.setPadding(0,0,0,0);

                    WindowManager.LayoutParams p = mDecorLayoutParams;
                    p.verticalMargin = 0;
                    p.horizontalMargin = 0;
                    p.width = mAnchor.getWidth();
                    p.gravity = Gravity.LEFT|Gravity.TOP;
                    p.x = anchorPos[0];// + (mAnchor.getWidth() - p.width) / 2;
                    p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
                    mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);




                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
