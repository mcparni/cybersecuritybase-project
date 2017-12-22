package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Table(name = "Users")
public class Users extends AbstractPersistable<Long>  {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
	private String username;
	@Column(name = "password")
    private String password;
	
	public Users() {
        super();
    }
	
	public Users(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }
	
	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
