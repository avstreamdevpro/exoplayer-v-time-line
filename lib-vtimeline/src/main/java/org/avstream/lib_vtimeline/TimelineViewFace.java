package org.avstream.lib_vtimeline;

import android.content.Context;

public interface TimelineViewFace {
    void startSurfaceRenderer();
    void releaseSurface();
    Context context();
    void attachVideoFactory(ExoPlayerFactory playerFactory);
    void setMediaUri(String mediaUri);
}
