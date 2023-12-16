package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Music type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Music", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Music implements Model {
  public static final QueryField ID = field("Music", "id");
  public static final QueryField MUSIC_TITLE = field("Music", "musicTitle");
  public static final QueryField MUSIC_ARTIST = field("Music", "musicArtist");
  public static final QueryField MUSIC_COVER = field("Music", "musicCover");
  public static final QueryField MUSIC_MP3 = field("Music", "musicMp3");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String musicTitle;
  private final @ModelField(targetType="String", isRequired = true) String musicArtist;
  private final @ModelField(targetType="String", isRequired = true) String musicCover;
  private final @ModelField(targetType="String", isRequired = true) String musicMp3;
  private final @ModelField(targetType="Playlist") @HasMany(associatedWith = "id", type = Playlist.class) List<Playlist> playlists = null;
  private final @ModelField(targetType="FavoriteMusic") @HasMany(associatedWith = "track", type = FavoriteMusic.class) List<FavoriteMusic> favoriteMusics = null;
  private final @ModelField(targetType="PlaylistMusic") @HasMany(associatedWith = "track", type = PlaylistMusic.class) List<PlaylistMusic> playlistMusic = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getMusicTitle() {
      return musicTitle;
  }
  
  public String getMusicArtist() {
      return musicArtist;
  }
  
  public String getMusicCover() {
      return musicCover;
  }
  
  public String getMusicMp3() {
      return musicMp3;
  }
  
  public List<Playlist> getPlaylists() {
      return playlists;
  }
  
  public List<FavoriteMusic> getFavoriteMusics() {
      return favoriteMusics;
  }
  
  public List<PlaylistMusic> getPlaylistMusic() {
      return playlistMusic;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Music(String id, String musicTitle, String musicArtist, String musicCover, String musicMp3) {
    this.id = id;
    this.musicTitle = musicTitle;
    this.musicArtist = musicArtist;
    this.musicCover = musicCover;
    this.musicMp3 = musicMp3;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Music music = (Music) obj;
      return ObjectsCompat.equals(getId(), music.getId()) &&
              ObjectsCompat.equals(getMusicTitle(), music.getMusicTitle()) &&
              ObjectsCompat.equals(getMusicArtist(), music.getMusicArtist()) &&
              ObjectsCompat.equals(getMusicCover(), music.getMusicCover()) &&
              ObjectsCompat.equals(getMusicMp3(), music.getMusicMp3()) &&
              ObjectsCompat.equals(getCreatedAt(), music.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), music.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getMusicTitle())
      .append(getMusicArtist())
      .append(getMusicCover())
      .append(getMusicMp3())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Music {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("musicTitle=" + String.valueOf(getMusicTitle()) + ", ")
      .append("musicArtist=" + String.valueOf(getMusicArtist()) + ", ")
      .append("musicCover=" + String.valueOf(getMusicCover()) + ", ")
      .append("musicMp3=" + String.valueOf(getMusicMp3()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static MusicTitleStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Music justId(String id) {
    return new Music(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      musicTitle,
      musicArtist,
      musicCover,
      musicMp3);
  }
  public interface MusicTitleStep {
    MusicArtistStep musicTitle(String musicTitle);
  }
  

  public interface MusicArtistStep {
    MusicCoverStep musicArtist(String musicArtist);
  }
  

  public interface MusicCoverStep {
    MusicMp3Step musicCover(String musicCover);
  }
  

  public interface MusicMp3Step {
    BuildStep musicMp3(String musicMp3);
  }
  

  public interface BuildStep {
    Music build();
    BuildStep id(String id);
  }
  

  public static class Builder implements MusicTitleStep, MusicArtistStep, MusicCoverStep, MusicMp3Step, BuildStep {
    private String id;
    private String musicTitle;
    private String musicArtist;
    private String musicCover;
    private String musicMp3;
    public Builder() {
      
    }
    
    private Builder(String id, String musicTitle, String musicArtist, String musicCover, String musicMp3) {
      this.id = id;
      this.musicTitle = musicTitle;
      this.musicArtist = musicArtist;
      this.musicCover = musicCover;
      this.musicMp3 = musicMp3;
    }
    
    @Override
     public Music build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Music(
          id,
          musicTitle,
          musicArtist,
          musicCover,
          musicMp3);
    }
    
    @Override
     public MusicArtistStep musicTitle(String musicTitle) {
        Objects.requireNonNull(musicTitle);
        this.musicTitle = musicTitle;
        return this;
    }
    
    @Override
     public MusicCoverStep musicArtist(String musicArtist) {
        Objects.requireNonNull(musicArtist);
        this.musicArtist = musicArtist;
        return this;
    }
    
    @Override
     public MusicMp3Step musicCover(String musicCover) {
        Objects.requireNonNull(musicCover);
        this.musicCover = musicCover;
        return this;
    }
    
    @Override
     public BuildStep musicMp3(String musicMp3) {
        Objects.requireNonNull(musicMp3);
        this.musicMp3 = musicMp3;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String musicTitle, String musicArtist, String musicCover, String musicMp3) {
      super(id, musicTitle, musicArtist, musicCover, musicMp3);
      Objects.requireNonNull(musicTitle);
      Objects.requireNonNull(musicArtist);
      Objects.requireNonNull(musicCover);
      Objects.requireNonNull(musicMp3);
    }
    
    @Override
     public CopyOfBuilder musicTitle(String musicTitle) {
      return (CopyOfBuilder) super.musicTitle(musicTitle);
    }
    
    @Override
     public CopyOfBuilder musicArtist(String musicArtist) {
      return (CopyOfBuilder) super.musicArtist(musicArtist);
    }
    
    @Override
     public CopyOfBuilder musicCover(String musicCover) {
      return (CopyOfBuilder) super.musicCover(musicCover);
    }
    
    @Override
     public CopyOfBuilder musicMp3(String musicMp3) {
      return (CopyOfBuilder) super.musicMp3(musicMp3);
    }
  }
  


  
}
