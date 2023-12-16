//package com.example.rhythmix;
//
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class Track implements Parcelable {
//
//    private long id;
//    private String title;
//    private String preview;
//
//    private Artist artist;
//    private Album album;
//
//
//    public Track(JSONObject musicObject) throws JSONException {
//        id = musicObject.getInt("id");
//        title = musicObject.getString("title");
//        preview = musicObject.getString("preview");
//
//        JSONObject artistObject = musicObject.getJSONObject("artist");
//        artist = new Artist(artistObject);
//
//        JSONObject albumObject = musicObject.getJSONObject("album");
//        album = new Album(albumObject);
//    }
//
//
//    public long getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getPreview() {
//        return preview;
//    }
//
//    public Artist getArtist() {
//        return artist;
//    }
//
//    public Album getAlbum() {
//        return album;
//    }
//
//
//    protected Track(Parcel in) {
//        id = in.readLong();
//        title = in.readString();
//        preview = in.readString();
//        artist = in.readParcelable(Artist.class.getClassLoader());
//        album = in.readParcelable(Album.class.getClassLoader());
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(id);
//        dest.writeString(title);
//        dest.writeString(preview);
//        dest.writeParcelable((Parcelable) artist, flags);
//        dest.writeParcelable((Parcelable) album, flags);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<Track> CREATOR = new Creator<Track>() {
//        @Override
//        public Track createFromParcel(Parcel in) {
//            return new Track(in);
//        }
//
//        @Override
//        public Track[] newArray(int size) {
//            return new Track[size];
//        }
//    };
//}