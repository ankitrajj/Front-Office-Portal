package in.ankit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ankit.binding.DashboardResponse;
import in.ankit.binding.EnquiryForm;
import in.ankit.binding.EnquirySearchCriteria;
import in.ankit.entity.StudentEnqEntity;
import in.ankit.services.EnquiryServiceImpl;
import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {
	
	@Autowired
	private EnquiryServiceImpl enqService;
	
	@Autowired
	private HttpSession session;
	
	
	@GetMapping("/dashboard")
	public String dashboardPage(Model model){
		Integer userId = (Integer) session.getAttribute("userId");
		DashboardResponse dashboardResponse = enqService.getDashboardResponse(userId);
		model.addAttribute("dashboardResponse",dashboardResponse);		
		return "dashboard";	
	}
	
	@GetMapping("/addenquiry")
	public String addEnquiryPage(Model model) {
		model.addAttribute("enqForm", new EnquiryForm());
		List<String> courseList = enqService.getCourses();
		model.addAttribute("courseList",courseList);	
		List<String> statusList = enqService.getEnqStatus();
		model.addAttribute("statusList",statusList);
		return "addEnquiry";	
	}
	
	@PostMapping("/addenquiry")
	public String handleEnquiry(@ModelAttribute("enqForm") EnquiryForm enqForm, Model model) {
		boolean status = enqService.upsertEnquiry(enqForm);
		if(status) {
			model.addAttribute("SuccessMsg","Enquiry Saved!!");
		}else {
			model.addAttribute("ErrorMsg", "Something went wrong!!");
		}
		return "addEnquiry";		
	}
	
	@GetMapping("/viewenquiry")
	public String viewEnquiryPage(Model model) {
		initForm(model);
		model.addAttribute("searchForm", new EnquirySearchCriteria());
		List<StudentEnqEntity> enquiries = enqService.getEnquiries();
		model.addAttribute("enquiries", enquiries);
		return "viewEnquiry";	
	}

	private void initForm(Model model) {
		List<String> courseList = enqService.getCourses();	
		List<String> statusList = enqService.getEnqStatus();
		EnquiryForm formObj = new EnquiryForm();	
		model.addAttribute("courseList",courseList);
		model.addAttribute("statusList",statusList);
		model.addAttribute("formObj", formObj);
	}
	
	
	@GetMapping("/filter-enquiries")
	public String filterEnquiries(@RequestParam String cname, @RequestParam String mode, @RequestParam String status, Model model) {		
		EnquirySearchCriteria criteria = new EnquirySearchCriteria();
		criteria.setCourseName(cname);
		criteria.setClassMode(mode);
		criteria.setEnquiryStatus(status);		
		
		List<StudentEnqEntity> filteredEnquiries = enqService.getFilteredEnquiries(criteria);
		model.addAttribute("enquiries", filteredEnquiries);
				
		return "enquiryTable";
		
	}
	
	@GetMapping("/delete/{id}")
	public String deleteEnquiry(@PathVariable Integer id, Model model) {
	    enqService.deleteEnquiry(id);
		return "viewEnquiry";		
	}
	
}
