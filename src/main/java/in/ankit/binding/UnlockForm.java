package in.ankit.binding;

import lombok.Data;

@Data
public class UnlockForm {
	 private String email;
     private String genPwd;
     private String newPwd;
     private String confirmPwd;
}
