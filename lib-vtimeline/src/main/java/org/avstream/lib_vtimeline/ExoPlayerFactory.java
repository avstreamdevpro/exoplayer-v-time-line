package org.avstream.lib_vtimeline;

import android.content.Context;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

public class ExoPlayerFactory {

    private SeekParameters seekParameters;
    private boolean preferSoftwareDecoder;
    private DataSource.Factory dataSourceFactory;

    ExoPlayerFactory(SeekParameters seekParameters) {
        this(seekParameters, false, null);
    }

    ExoPlayerFactory(SeekParameters seekParameters,
                     boolean preferSoftwareDecoder,
                     DataSource.Factory dataSourceFactory) {
        this.seekParameters = seekParameters;
        this.preferSoftwareDecoder = preferSoftwareDecoder;
        this.dataSourceFactory = dataSourceFactory;
    }

    public ExoPlayer getPlayer(Context context) {

        int bufferMs = preferSoftwareDecoder ? 100 : 2000;

        ExoPlayer exoPlayer = new ExoPlayer.Builder(context, new VideoRendererOnlyFactory(context, preferSoftwareDecoder))
                .setLoadControl(
                        new DefaultLoadControl.Builder()
                                .setBufferDurationsMs(bufferMs, bufferMs, 0, 0).build())
                .build();
        exoPlayer.setSeekParameters(seekParameters);
        return exoPlayer;
    }

    public MediaSource getMediaSource(String mediaUri, Context context) {

        if (dataSourceFactory == null) {
            dataSourceFactory = new DefaultDataSource.Factory(context);
        }
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(mediaUri));
    }
}
