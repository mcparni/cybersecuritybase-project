package sec.project.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;
import sec.project.config.SensitiveDataConverter;

@Entity
@Table(name = "Signup")
public class Signup extends AbstractPersistable<Long> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    @Column(name = "name")
	@Convert(converter = SensitiveDataConverter.class)	
	private String name;
	@Column(name = "address")
	@Convert(converter = SensitiveDataConverter.class)
    private String address;
	@Column(name = "credit_card")
	@Convert(converter = SensitiveDataConverter.class)
    private String ccnr;

    public Signup() {
        super();
    }
	
    public Signup(String name, String address, String ccnr) {
        this();
        this.name = name;
        this.address = address;
		this.ccnr = ccnr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
	
	public String getCcnr() {
        return ccnr;
    }

    public void setCCnr(String ccnr) {
        this.ccnr = ccnr;
    }

}
