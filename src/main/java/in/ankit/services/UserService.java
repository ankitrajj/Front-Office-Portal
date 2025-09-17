package in.ankit.services;

import in.ankit.binding.LoginForm;
import in.ankit.binding.SignUpForm;
import in.ankit.binding.UnlockForm;

public interface UserService {
	
	public String login(LoginForm form);
	public boolean signup(SignUpForm form);
	public boolean unlockAccount(UnlockForm form);
	public boolean forgotPwd(String email);

}
