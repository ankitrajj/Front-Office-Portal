package in.ankit.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ankit.binding.LoginForm;
import in.ankit.binding.SignUpForm;
import in.ankit.binding.UnlockForm;
import in.ankit.entity.UserDtlsEntity;
import in.ankit.repo.UserDtlsRepo;
import in.ankit.utilities.EmailUtils;
import in.ankit.utilities.PwdUtils;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDtlsRepo userRepo;
	
	@Autowired
	private EmailUtils emailSender;
	
	@Autowired
	private HttpSession session;
	
	@Override
	public boolean signup(SignUpForm form) {
		
		UserDtlsEntity users = userRepo.findByEmail(form.getEmail());
		if(users != null) {
			return false;
		}
		
		UserDtlsEntity entity = new UserDtlsEntity(); //Copy data from binding object to entity.
		BeanUtils.copyProperties(form, entity);
		
		String tempPwd = PwdUtils.generateRandomPwd();
		
		entity.setAccStatus("Locked");            //Set account status as locked.	
		entity.setPwd(tempPwd);
		userRepo.save(entity);                    // Insert record in the table.
		
		String toEmail = form.getEmail();
		String subject = "Unlock your account";
		
		StringBuffer sb = new StringBuffer();
		sb.append("<h5>Welcome to fornk deak!! Unlock your account with the below link and given password </h5>");
		sb.append("Generated Password: "+ tempPwd + "<br>");
		sb.append("<a href=\"http://localhost:8081/unlock?email="+ toEmail +"\">Click here to unlock your account!!</a>");			
		boolean mailStatus = emailSender.sendEmail(toEmail, subject, sb.toString());
		return mailStatus;
	}
	
	@Override
	public boolean unlockAccount(UnlockForm form) {
		UserDtlsEntity entity = userRepo.findByEmail(form.getEmail());
		if(entity.getPwd().equals(form.getGenPwd())) {
			entity.setAccStatus("Unlock");
			entity.setPwd(form.getNewPwd());
			userRepo.save(entity);
			return true;		
		}else {
			return false;
		}	
	}

	
	
	@Override
	public String login(LoginForm form) {
		UserDtlsEntity entity = userRepo.findByEmailAndPwd(form.getEmail(), form.getPassword());
		if(entity==null) {
			return "Please Sign Up!!";
		}
		
		if(entity.getAccStatus().equals("Locked")) {
			return "Unlock your account!!";
		}
		
		session.setAttribute("userId", entity.getUserId());
		return "Success";

	}
	
	@Override
	public boolean forgotPwd(String email) {
		UserDtlsEntity entity = userRepo.findByEmail(email);
		if(entity==null) {   // To avoid null pointer exception.
			return false;
		}
			String subject = "Recover your Paasword!!";
			String body = "Your Password: "+entity.getPwd();
			boolean status = emailSender.sendEmail(email, subject, body);      
			return status;				
	}
}
