package in.ankit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ankit.binding.LoginForm;
import in.ankit.binding.SignUpForm;
import in.ankit.binding.UnlockForm;
import in.ankit.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private  UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";	
	}
	
	@PostMapping("/signup")
	public String handlesignup(@ModelAttribute("user") SignUpForm form, Model model) {
		boolean status = userService.signup(form);
		if(status) {
			model.addAttribute("SuccessMsg", "Account Created!!, Check your mail.");
		}else {
			model.addAttribute("ErrorMsg", "Enter an unique email!!");
		}
		return "signup";	
	}
	
	
	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {
		UnlockForm unlock = new UnlockForm();
	    unlock.setEmail(email);
		model.addAttribute("unlock", unlock);
		return "unlock";	
	}
	
	@PostMapping("/unlock")
	public String handleUnlock(@ModelAttribute("unlock") UnlockForm unlock, Model model) {
	//	System.out.println(unlock);
		if(unlock.getNewPwd().equals(unlock.getConfirmPwd())) {
			boolean status = userService.unlockAccount(unlock);
			if(status) {
				  model.addAttribute("SuccessMsg", "Your account unlocked!!");
			}else {
				model.addAttribute("ErrorMsg1", "Enter correct generated password!!");
			}
		}else {
			model.addAttribute("ErrorMsg2", "New Password and Confirm Password should match.!!");
		}
		return "unlock";	
	}
	
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("loginObj", new LoginForm()); 
		return "login";	
	}
	
	@PostMapping("/login")
	public String handelLogin(@ModelAttribute("loginObj") LoginForm form,Model model) {
		String logIn = userService.login(form);	
		if(logIn.equals("Success")) {
			return "redirect:/dashboard";   // Redirects the user to the dashboard page when login is successful.
		}
		model.addAttribute("ErrorMsg",logIn);
		return "login";
	}

	

	@GetMapping("/forgotPwd")
	public String forgotpwdPage() {
		return "forgotPwd";	
	}
	
	@PostMapping("/forgotPwd")
	public String handleForgotpwd(@RequestParam("email") String email, Model model) {
		//System.out.println(email);  //{Use name="email" for request parameter, and th:object attribute is not required.}
		boolean status = userService.forgotPwd(email);
		if(status) {
			model.addAttribute("SuccessMsg", "Check Your mail!!");
		}else {
			model.addAttribute("ErrorMsg", "Enter a correct mail!!");
		}	
		
		return "forgotPwd";	
	}
	
			
	@GetMapping("/logout")
	public String logout() {		
	 session.invalidate();
	 return "index";		
	}
}
