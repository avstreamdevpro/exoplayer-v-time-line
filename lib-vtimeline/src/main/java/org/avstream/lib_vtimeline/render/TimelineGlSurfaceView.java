package org.avstream.lib_vtimeline.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.video.VideoSize;
import com.otaliastudios.opengl.core.EglConfigChooser;
import com.otaliastudios.opengl.core.EglContextFactory;

import org.avstream.lib_vtimeline.ExoPlayerFactory;
import org.avstream.lib_vtimeline.TimelineViewFace;

public class TimelineGlSurfaceView extends GLSurfaceView implements TimelineViewFace, Player.Listener, GlRenderer.SurfaceEventListener {

    private final Handler mainHandler;
    private GlRenderer renderer;
    private ExoPlayer player;
    private ExoPlayerFactory videoPlayerFactory;
    private String mediaUri;

    public TimelineGlSurfaceView(Context context) {
        this(context, null);
    }

    public TimelineGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVisibility(GONE);
        mainHandler = new Handler(Looper.getMainLooper());

        setEGLContextFactory(EglContextFactory.GLES2);
        setEGLConfigChooser(EglConfigChooser.GLES2);

        setRenderer();
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void setRenderer() {
        renderer = new GlRenderer(this);
        setRenderer(renderer);
    }

    @Override
    public void onVideoSizeChanged(VideoSize videoSize) {
        int width = videoSize.width;
        int height = videoSize.height;
        float ratio = videoSize.pixelWidthHeightRatio;
        float videoAspect = ((float) width / height) * ratio;
        renderer.onAspectPrepared(videoAspect);
        requestLayout();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void startSurfaceRenderer() {
        setVisibility(VISIBLE);
        renderer.setStartRendering();

        player = videoPlayerFactory.getPlayer(getContext());
        player.setMediaSource(videoPlayerFactory.getMediaSource(mediaUri, getContext()));
        player.prepare();
        player.addListener(this);
    }

    @Override
    public void releaseSurface() {
        renderer.release();
        onFinish();
    }

    @Override
    public void onSurfaceAvailable(Surface surface) {
        mainHandler.post(() -> {
            this.player.setVideoSurface(surface);
        });
    }

    @Override
    public void nextFrame(int offset, int limit) {
        requestRender();
        if (offset < limit) {
            long videoDuration = player.getDuration();
            long seekPos =
                    Math.max(videoDuration / limit + offset * (videoDuration / limit), player.getCurrentPosition() + 1);

            player.seekTo(seekPos);
        }
    }

    @Override
    public void onFinish() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void attachVideoFactory(ExoPlayerFactory playerFactory) {
        this.videoPlayerFactory = playerFactory;
    }

    @Override
    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }
}