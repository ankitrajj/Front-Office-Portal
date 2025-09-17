package in.ankit.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class UserDtlsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String name;
	
//	@Column(unique = true)
	private String email;
	
	private Integer phno;
	private String pwd;
	
//	@Column(columnDefinition = "VARCHAR(255) DEFAULT 'Locked'")
   	private String accStatus;
   	
   	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
   	private List<StudentEnqEntity> enquiries;
   	
}
