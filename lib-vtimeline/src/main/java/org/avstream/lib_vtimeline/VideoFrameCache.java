package org.avstream.lib_vtimeline;

import androidx.annotation.Nullable;

import org.avstream.lib_vtimeline.tools.FileHelper;

import java.io.File;

public class VideoFrameCache {

    private File cacheDir;

    VideoFrameCache(File cacheDir) {
        this.cacheDir = cacheDir;
        if (cacheDir != null) {
            cacheDir.mkdir();
        }
    }

    public File getCacheDir() {
        return cacheDir;
    }

    private String mediaId(String media) {
        return media.substring(media.lastIndexOf('/') + 1);
    }

    @Nullable
    File fileAt(String video, long timeMs) {
        return cacheDir != null ? FileHelper.getCachedFile(cacheDir, mediaId(video), timeMs) : null;
    }
}
