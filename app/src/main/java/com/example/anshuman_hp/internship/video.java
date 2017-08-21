package com.example.anshuman_hp.internship;

/**
 * Created by Anshuman-HP on 20-08-2017.
 */

public class video {

    String videoThumbnailUrl;
    String videoCaption;
    String videoDuration;
    String videoUrl;
    String videoID;

    public video() {
    }

    public video(String videoThumbnailUrl, String videoCaption, String videoDuration, String videoUrl, String videoID) {
        this.videoThumbnailUrl = videoThumbnailUrl;
        this.videoCaption = videoCaption;
        this.videoDuration = videoDuration;
        this.videoUrl = videoUrl;
        this.videoID = videoID;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoThumbnailUrl() {
        return videoThumbnailUrl;
    }

    public void setVideoThumbnailUrl(String videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    public String getVideoCaption() {
        return videoCaption;
    }

    public void setVideoCaption(String videoCaption) {
        this.videoCaption = videoCaption;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }
}
