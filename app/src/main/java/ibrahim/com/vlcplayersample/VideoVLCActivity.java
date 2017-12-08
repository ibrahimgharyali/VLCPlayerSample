package ibrahim.com.vlcplayersample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;

public class VideoVLCActivity extends Activity implements IVideoPlayer {
    private static final String TAG = VideoVLCActivity.class.getSimpleName();

    private LibVLC mLibVLC;

    private String mMediaUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "VideoVLC -- onCreate -- START ------------");
        setContentView(R.layout.activity_video_vlc);

        ImageView mStartButton = (ImageView) findViewById(R.id.start_button);
        SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.player_surface);
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();

        mMediaUrl = getIntent().getExtras().getString("videoUrl");

        try {
            mLibVLC = new LibVLC();
            mLibVLC.setAout(LibVLC.AOUT_AUDIOTRACK);
            mLibVLC.setVout(LibVLC.VOUT_ANDROID_SURFACE);
            mLibVLC.setHardwareAcceleration(LibVLC.HW_ACCELERATION_FULL);

            mLibVLC.init(getApplicationContext());
        } catch (LibVlcException e){
            Log.e(TAG, e.toString());
        }

        Surface mSurface = mSurfaceHolder.getSurface();

        mLibVLC.attachSurface(mSurface, VideoVLCActivity.this);
        mLibVLC.playMRL(mMediaUrl);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLibVLC.stop();
                mLibVLC.playMRL(mMediaUrl);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaCodec opaque direct rendering should not be used anymore since there is no surface to attach.
        mLibVLC.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_vlc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void eventHardwareAccelerationError() {
        Log.e(TAG, "eventHardwareAccelerationError()!");
        return;
    }

    @Override
    public void setSurfaceLayout(final int width, final int height, int visible_width, int visible_height, final int sar_num, int sar_den){
        Log.d(TAG, "setSurfaceSize -- START");
        if (width * height == 0)
            return;
        Log.d(TAG, "setSurfaceSize -- mMediaUrl: " + mMediaUrl + " mVideoHeight: " + height + " mVideoWidth: " + width + " mVideoVisibleHeight: " + visible_height + " mVideoVisibleWidth: " + visible_width + " mSarNum: " + sar_num + " mSarDen: " + sar_den);
    }

    @Override
    public int configureSurface(android.view.Surface surface, int i, int i1, int i2){
        return -1;
    }
}
