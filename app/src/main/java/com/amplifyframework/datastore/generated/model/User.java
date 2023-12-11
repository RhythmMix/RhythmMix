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

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users",  authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class User implements Model {
  public static final QueryField ID = field("User", "id");
  public static final QueryField USER_ID = field("User", "userID");
  public static final QueryField EMAIL = field("User", "email");
  public static final QueryField USERNAME = field("User", "username");
  public static final QueryField PASSWORD = field("User", "password");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String userID;
  private final @ModelField(targetType="String", isRequired = true) String email;
  private final @ModelField(targetType="String") String username;
  private final @ModelField(targetType="String", isRequired = true) String password;
  private final @ModelField(targetType="Playlist") @HasMany(associatedWith = "user", type = Playlist.class) List<Playlist> playlists = null;
  private final @ModelField(targetType="FavoriteMusic") @HasMany(associatedWith = "id", type = FavoriteMusic.class) List<FavoriteMusic> favorites = null;
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
  
  public String getUserId() {
      return userID;
  }
  
  public String getEmail() {
      return email;
  }
  
  public String getUsername() {
      return username;
  }
  
  public String getPassword() {
      return password;
  }
  
  public List<Playlist> getPlaylists() {
      return playlists;
  }
  
  public List<FavoriteMusic> getFavorites() {
      return favorites;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private User(String id, String userID, String email, String username, String password) {
    this.id = id;
    this.userID = userID;
    this.email = email;
    this.username = username;
    this.password = password;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getUserId(), user.getUserId()) &&
              ObjectsCompat.equals(getEmail(), user.getEmail()) &&
              ObjectsCompat.equals(getUsername(), user.getUsername()) &&
              ObjectsCompat.equals(getPassword(), user.getPassword()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getEmail())
      .append(getUsername())
      .append(getPassword())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userID=" + String.valueOf(getUserId()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("password=" + String.valueOf(getPassword()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
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
  public static User justId(String id) {
    return new User(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userID,
      email,
      username,
      password);
  }
  public interface UserIdStep {
    EmailStep userId(String userId);
  }
  

  public interface EmailStep {
    PasswordStep email(String email);
  }
  

  public interface PasswordStep {
    BuildStep password(String password);
  }
  

  public interface BuildStep {
    User build();
    BuildStep id(String id);
    BuildStep username(String username);
  }
  

  public static class Builder implements UserIdStep, EmailStep, PasswordStep, BuildStep {
    private String id;
    private String userID;
    private String email;
    private String password;
    private String username;
    public Builder() {
      
    }
    
    private Builder(String id, String userID, String email, String username, String password) {
      this.id = id;
      this.userID = userID;
      this.email = email;
      this.username = username;
      this.password = password;
    }
    
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          userID,
          email,
          username,
          password);
    }
    
    @Override
     public EmailStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userID = userId;
        return this;
    }
    
    @Override
     public PasswordStep email(String email) {
        Objects.requireNonNull(email);
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep password(String password) {
        Objects.requireNonNull(password);
        this.password = password;
        return this;
    }
    
    @Override
     public BuildStep username(String username) {
        this.username = username;
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
    private CopyOfBuilder(String id, String userId, String email, String username, String password) {
      super(id, userID, email, username, password);
      Objects.requireNonNull(userID);
      Objects.requireNonNull(email);
      Objects.requireNonNull(password);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder password(String password) {
      return (CopyOfBuilder) super.password(password);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
  }

  
}
