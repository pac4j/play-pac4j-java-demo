package models;

/**
 * 
 */

import play.data.validation.Constraints;
import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class User extends Model{
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;
	
    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    public User() {}
    public User(String username, String password) { this.username = username; this.password = password;}

}
