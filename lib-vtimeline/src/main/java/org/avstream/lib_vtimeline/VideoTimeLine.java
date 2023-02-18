package org.avstream.lib_vtimeline;

import com.google.android.exoplayer2.SeekParameters;

public class VideoTimeLine {

    private TimelineViewFace timelineView;
    private boolean started;

    public void start() {
        if (started) return;

        started = true;
        timelineView.startSurfaceRenderer();
    }

    public void destroy() {
        release();
    }

    private void release() {
        timelineView.releaseSurface();
    }

    public static Builder with(String mediaUri) {
        return new Builder(mediaUri);
    }

    public static class Builder {
        private final String mediaURI;

        Builder(String mediaURI) {
            this.mediaURI = mediaURI;
        }

        public VideoTimeLine into(TimelineViewFace fixedView) {
            VideoTimeLine timeline = new VideoTimeLine();
            timeline.timelineView = fixedView;
            timeline.timelineView.setMediaUri(mediaURI);
            timeline.timelineView.attachVideoFactory(new ExoPlayerFactory(SeekParameters.NEXT_SYNC));
            return timeline;
        }

        public VideoTimeLine show(TimelineViewFace view) {
            VideoTimeLine timeLine = into(view);
            timeLine.start();
            return timeLine;
        }
    }
}
