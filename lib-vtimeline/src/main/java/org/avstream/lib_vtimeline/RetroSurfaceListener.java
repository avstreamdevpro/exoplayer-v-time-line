package org.avstream.lib_vtimeline;

import android.view.Surface;

import java.nio.ByteBuffer;

public interface RetroSurfaceListener {
    void onSurfaceAvailable(Surface surface);
    void onTextureRetrieved(ByteBuffer pixelBuffer);
}
