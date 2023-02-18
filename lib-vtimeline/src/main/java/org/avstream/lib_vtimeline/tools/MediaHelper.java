package org.avstream.lib_vtimeline.tools;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import org.avstream.lib_vtimeline.VideoMetadata;

public class MediaHelper {

    public static void getVideoMets(Context context, String media, VideoMetadata metadata) {

        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {

            retriever.setDataSource(context, Uri.parse(media));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String widthString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String heightString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);

            metadata.setDurationMs(!TextUtils.isEmpty(time) ? Long.parseLong(time) : getDuration(context, media));
            metadata.setWidth(!TextUtils.isEmpty(widthString) ? Integer.parseInt(widthString) : 0);
            metadata.setHeight(!TextUtils.isEmpty(heightString) ? Integer.parseInt(heightString) : 0);
            retriever.release();

        } catch (Exception e) {
            e.printStackTrace();
            Loggy.d("Error retrieving: " + e);
        }

    }

    private static long getDuration(Context context, String media) {
        if (media.startsWith("content://")) {
            return getContentDuration(context, media);
        } else {
            return getFileDuration(context, media);
        }
    }


    private static long getContentDuration(Context context, String media) {
        Uri uri = Uri.parse(media);

        long duration = 0;
        Cursor cursor = MediaStore.Video.query(context.getContentResolver(),
                uri, new String[] {"duration"});
        if (cursor != null && cursor.moveToFirst()) {
            try {
                duration = cursor.getLong(cursor.getColumnIndexOrThrow("duration"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }

        return duration;
    }

    private static long getFileDuration(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[] {"duration"},
                MediaStore.Video.Media.DATA + "=?",
                new String[] { filePath } ,
                null);

        long duration = 0;
        if (cursor != null && cursor.moveToFirst()) {
            try {
                cursor.getLong(cursor.getColumnIndexOrThrow("duration"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return duration;
    }
}
