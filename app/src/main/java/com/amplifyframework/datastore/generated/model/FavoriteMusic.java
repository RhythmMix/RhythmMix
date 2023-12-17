package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
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

/** This is an auto generated class representing the FavoriteMusic type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "FavoriteMusics", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class FavoriteMusic implements Model {
  public static final QueryField ID = field("FavoriteMusic", "id");
  public static final QueryField FAVORITE = field("FavoriteMusic", "favoriteID");
  public static final QueryField TRACK = field("FavoriteMusic", "musicID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Favorite") @BelongsTo(targetName = "favoriteID",  type = Favorite.class) Favorite favorite;
  private final @ModelField(targetType="Music") @BelongsTo(targetName = "musicID",  type = Music.class) Music track;
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
  
  public Favorite getFavorite() {
      return favorite;
  }
  
  public Music getTrack() {
      return track;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private FavoriteMusic(String id, Favorite favorite, Music track) {
    this.id = id;
    this.favorite = favorite;
    this.track = track;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      FavoriteMusic favoriteMusic = (FavoriteMusic) obj;
      return ObjectsCompat.equals(getId(), favoriteMusic.getId()) &&
              ObjectsCompat.equals(getFavorite(), favoriteMusic.getFavorite()) &&
              ObjectsCompat.equals(getTrack(), favoriteMusic.getTrack()) &&
              ObjectsCompat.equals(getCreatedAt(), favoriteMusic.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), favoriteMusic.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getFavorite())
      .append(getTrack())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("FavoriteMusic {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("favorite=" + String.valueOf(getFavorite()) + ", ")
      .append("track=" + String.valueOf(getTrack()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
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
  public static FavoriteMusic justId(String id) {
    return new FavoriteMusic(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      favorite,
      track);
  }
  public interface BuildStep {
    FavoriteMusic build();
    BuildStep id(String id);
    BuildStep favorite(Favorite favorite);
    BuildStep track(Music track);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private Favorite favorite;
    private Music track;
    public Builder() {
      
    }
    
    private Builder(String id, Favorite favorite, Music track) {
      this.id = id;
      this.favorite = favorite;
      this.track = track;
    }
    
    @Override
     public FavoriteMusic build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new FavoriteMusic(
          id,
          favorite,
          track);
    }
    
    @Override
     public BuildStep favorite(Favorite favorite) {
        this.favorite = favorite;
        return this;
    }
    
    @Override
     public BuildStep track(Music track) {
        this.track = track;
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
    private CopyOfBuilder(String id, Favorite favorite, Music track) {
      super(id, favorite, track);
      
    }
    
    @Override
     public CopyOfBuilder favorite(Favorite favorite) {
      return (CopyOfBuilder) super.favorite(favorite);
    }
    
    @Override
     public CopyOfBuilder track(Music track) {
      return (CopyOfBuilder) super.track(track);
    }
  }
  


  
}
