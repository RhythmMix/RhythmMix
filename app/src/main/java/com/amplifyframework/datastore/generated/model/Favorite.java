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

/** This is an auto generated class representing the Favorite type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Favorites", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Favorite implements Model {
  public static final QueryField ID = field("Favorite", "id");
  public static final QueryField FAVORITE_ID = field("Favorite", "favoriteID");
  public static final QueryField FAVORITE_TITLE = field("Favorite", "favoriteTitle");
  public static final QueryField FAVORITE_ARTIST = field("Favorite", "favoriteArtist");
  public static final QueryField FAVORITE_MP3 = field("Favorite", "favoriteMp3");
  public static final QueryField FAVORITE_COVER = field("Favorite", "favoriteCover");
  public static final QueryField USER_ID = field("Favorite", "userID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String favoriteID;
  private final @ModelField(targetType="String", isRequired = true) String favoriteTitle;
  private final @ModelField(targetType="String", isRequired = true) String favoriteArtist;
  private final @ModelField(targetType="String", isRequired = true) String favoriteMp3;
  private final @ModelField(targetType="String") String favoriteCover;
  private final @ModelField(targetType="ID", isRequired = true) String userID;
  private final @ModelField(targetType="FavoriteMusic") @HasMany(associatedWith = "favorite", type = FavoriteMusic.class) List<FavoriteMusic> favoriteMusics = null;
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
  
  public String getFavoriteId() {
      return favoriteID;
  }
  
  public String getFavoriteTitle() {
      return favoriteTitle;
  }
  
  public String getFavoriteArtist() {
      return favoriteArtist;
  }
  
  public String getFavoriteMp3() {
      return favoriteMp3;
  }
  
  public String getFavoriteCover() {
      return favoriteCover;
  }
  
  public String getUserId() {
      return userID;
  }
  
  public List<FavoriteMusic> getFavoriteMusics() {
      return favoriteMusics;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Favorite(String id, String favoriteID, String favoriteTitle, String favoriteArtist, String favoriteMp3, String favoriteCover, String userID) {
    this.id = id;
    this.favoriteID = favoriteID;
    this.favoriteTitle = favoriteTitle;
    this.favoriteArtist = favoriteArtist;
    this.favoriteMp3 = favoriteMp3;
    this.favoriteCover = favoriteCover;
    this.userID = userID;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Favorite favorite = (Favorite) obj;
      return ObjectsCompat.equals(getId(), favorite.getId()) &&
              ObjectsCompat.equals(getFavoriteId(), favorite.getFavoriteId()) &&
              ObjectsCompat.equals(getFavoriteTitle(), favorite.getFavoriteTitle()) &&
              ObjectsCompat.equals(getFavoriteArtist(), favorite.getFavoriteArtist()) &&
              ObjectsCompat.equals(getFavoriteMp3(), favorite.getFavoriteMp3()) &&
              ObjectsCompat.equals(getFavoriteCover(), favorite.getFavoriteCover()) &&
              ObjectsCompat.equals(getUserId(), favorite.getUserId()) &&
              ObjectsCompat.equals(getCreatedAt(), favorite.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), favorite.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getFavoriteId())
      .append(getFavoriteTitle())
      .append(getFavoriteArtist())
      .append(getFavoriteMp3())
      .append(getFavoriteCover())
      .append(getUserId())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Favorite {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("favoriteID=" + String.valueOf(getFavoriteId()) + ", ")
      .append("favoriteTitle=" + String.valueOf(getFavoriteTitle()) + ", ")
      .append("favoriteArtist=" + String.valueOf(getFavoriteArtist()) + ", ")
      .append("favoriteMp3=" + String.valueOf(getFavoriteMp3()) + ", ")
      .append("favoriteCover=" + String.valueOf(getFavoriteCover()) + ", ")
      .append("userID=" + String.valueOf(getUserId()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static FavoriteIdStep builder() {
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
  public static Favorite justId(String id) {
    return new Favorite(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      favoriteID,
      favoriteTitle,
      favoriteArtist,
      favoriteMp3,
      favoriteCover,
      userID);
  }
  public interface FavoriteIdStep {
    FavoriteTitleStep favoriteId(String favoriteId);
  }
  

  public interface FavoriteTitleStep {
    FavoriteArtistStep favoriteTitle(String favoriteTitle);
  }
  

  public interface FavoriteArtistStep {
    FavoriteMp3Step favoriteArtist(String favoriteArtist);
  }
  

  public interface FavoriteMp3Step {
    UserIdStep favoriteMp3(String favoriteMp3);
  }
  

  public interface UserIdStep {
    BuildStep userId(String userId);
  }
  

  public interface BuildStep {
    Favorite build();
    BuildStep id(String id);
    BuildStep favoriteCover(String favoriteCover);
  }
  

  public static class Builder implements FavoriteIdStep, FavoriteTitleStep, FavoriteArtistStep, FavoriteMp3Step, UserIdStep, BuildStep {
    private String id;
    private String favoriteID;
    private String favoriteTitle;
    private String favoriteArtist;
    private String favoriteMp3;
    private String userID;
    private String favoriteCover;
    public Builder() {
      
    }
    
    private Builder(String id, String favoriteID, String favoriteTitle, String favoriteArtist, String favoriteMp3, String favoriteCover, String userID) {
      this.id = id;
      this.favoriteID = favoriteID;
      this.favoriteTitle = favoriteTitle;
      this.favoriteArtist = favoriteArtist;
      this.favoriteMp3 = favoriteMp3;
      this.favoriteCover = favoriteCover;
      this.userID = userID;
    }
    
    @Override
     public Favorite build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Favorite(
          id,
          favoriteID,
          favoriteTitle,
          favoriteArtist,
          favoriteMp3,
          favoriteCover,
          userID);
    }
    
    @Override
     public FavoriteTitleStep favoriteId(String favoriteId) {
        Objects.requireNonNull(favoriteId);
        this.favoriteID = favoriteId;
        return this;
    }
    
    @Override
     public FavoriteArtistStep favoriteTitle(String favoriteTitle) {
        Objects.requireNonNull(favoriteTitle);
        this.favoriteTitle = favoriteTitle;
        return this;
    }
    
    @Override
     public FavoriteMp3Step favoriteArtist(String favoriteArtist) {
        Objects.requireNonNull(favoriteArtist);
        this.favoriteArtist = favoriteArtist;
        return this;
    }
    
    @Override
     public UserIdStep favoriteMp3(String favoriteMp3) {
        Objects.requireNonNull(favoriteMp3);
        this.favoriteMp3 = favoriteMp3;
        return this;
    }
    
    @Override
     public BuildStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userID = userId;
        return this;
    }
    
    @Override
     public BuildStep favoriteCover(String favoriteCover) {
        this.favoriteCover = favoriteCover;
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
    private CopyOfBuilder(String id, String favoriteId, String favoriteTitle, String favoriteArtist, String favoriteMp3, String favoriteCover, String userId) {
      super(id, favoriteID, favoriteTitle, favoriteArtist, favoriteMp3, favoriteCover, userID);
      Objects.requireNonNull(favoriteID);
      Objects.requireNonNull(favoriteTitle);
      Objects.requireNonNull(favoriteArtist);
      Objects.requireNonNull(favoriteMp3);
      Objects.requireNonNull(userID);
    }
    
    @Override
     public CopyOfBuilder favoriteId(String favoriteId) {
      return (CopyOfBuilder) super.favoriteId(favoriteId);
    }
    
    @Override
     public CopyOfBuilder favoriteTitle(String favoriteTitle) {
      return (CopyOfBuilder) super.favoriteTitle(favoriteTitle);
    }
    
    @Override
     public CopyOfBuilder favoriteArtist(String favoriteArtist) {
      return (CopyOfBuilder) super.favoriteArtist(favoriteArtist);
    }
    
    @Override
     public CopyOfBuilder favoriteMp3(String favoriteMp3) {
      return (CopyOfBuilder) super.favoriteMp3(favoriteMp3);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder favoriteCover(String favoriteCover) {
      return (CopyOfBuilder) super.favoriteCover(favoriteCover);
    }
  }
  


  
}
