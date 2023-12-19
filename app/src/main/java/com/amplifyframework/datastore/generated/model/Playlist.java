package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
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

/** This is an auto generated class representing the Playlist type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Playlists", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Playlist implements Model {
  public static final QueryField ID = field("Playlist", "id");
  public static final QueryField PLAYLIST_NAME = field("Playlist", "playlistName");
  public static final QueryField PLAYLIST_BACKGROUND = field("Playlist", "playlistBackground");
  public static final QueryField USER = field("Playlist", "userID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String playlistName;
  private final @ModelField(targetType="String") String playlistBackground;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userID",  type = User.class) User user;
  private final @ModelField(targetType="Music") @HasMany(associatedWith = "id", type = Music.class) List<Music> musics = null;
  private final @ModelField(targetType="PlaylistMusic") @HasMany(associatedWith = "playlist", type = PlaylistMusic.class) List<PlaylistMusic> playlistMusic = null;
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
  
  public String getPlaylistName() {
      return playlistName;
  }
  
  public String getPlaylistBackground() {
      return playlistBackground;
  }
  
  public User getUser() {
      return user;
  }
  
  public List<Music> getMusics() {
      return musics;
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
  
  private Playlist(String id, String playlistName, String playlistBackground, User user) {
    this.id = id;
    this.playlistName = playlistName;
    this.playlistBackground = playlistBackground;
    this.user = user;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Playlist playlist = (Playlist) obj;
      return ObjectsCompat.equals(getId(), playlist.getId()) &&
              ObjectsCompat.equals(getPlaylistName(), playlist.getPlaylistName()) &&
              ObjectsCompat.equals(getPlaylistBackground(), playlist.getPlaylistBackground()) &&
              ObjectsCompat.equals(getUser(), playlist.getUser()) &&
              ObjectsCompat.equals(getCreatedAt(), playlist.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), playlist.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getPlaylistName())
      .append(getPlaylistBackground())
      .append(getUser())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Playlist {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("playlistName=" + String.valueOf(getPlaylistName()) + ", ")
      .append("playlistBackground=" + String.valueOf(getPlaylistBackground()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static PlaylistNameStep builder() {
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
  public static Playlist justId(String id) {
    return new Playlist(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      playlistName,
      playlistBackground,
      user);
  }
  public interface PlaylistNameStep {
    BuildStep playlistName(String playlistName);
  }
  

  public interface BuildStep {
    Playlist build();
    BuildStep id(String id);
    BuildStep playlistBackground(String playlistBackground);
    BuildStep user(User user);
  }
  

  public static class Builder implements PlaylistNameStep, BuildStep {
    private String id;
    private String playlistName;
    private String playlistBackground;
    private User user;
    public Builder() {
      
    }
    
    private Builder(String id, String playlistName, String playlistBackground, User user) {
      this.id = id;
      this.playlistName = playlistName;
      this.playlistBackground = playlistBackground;
      this.user = user;
    }
    
    @Override
     public Playlist build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Playlist(
          id,
          playlistName,
          playlistBackground,
          user);
    }
    
    @Override
     public BuildStep playlistName(String playlistName) {
        Objects.requireNonNull(playlistName);
        this.playlistName = playlistName;
        return this;
    }
    
    @Override
     public BuildStep playlistBackground(String playlistBackground) {
        this.playlistBackground = playlistBackground;
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.user = user;
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
    private CopyOfBuilder(String id, String playlistName, String playlistBackground, User user) {
      super(id, playlistName, playlistBackground, user);
      Objects.requireNonNull(playlistName);
    }
    
    @Override
     public CopyOfBuilder playlistName(String playlistName) {
      return (CopyOfBuilder) super.playlistName(playlistName);
    }
    
    @Override
     public CopyOfBuilder playlistBackground(String playlistBackground) {
      return (CopyOfBuilder) super.playlistBackground(playlistBackground);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
  }
  


}
