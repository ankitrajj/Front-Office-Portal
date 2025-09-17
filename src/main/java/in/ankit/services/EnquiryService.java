package in.ankit.services;

import java.util.List;

import in.ankit.binding.DashboardResponse;
import in.ankit.binding.EnquiryForm;
import in.ankit.binding.EnquirySearchCriteria;
import in.ankit.entity.StudentEnqEntity;

public interface EnquiryService {
	
	public List<String> getCourses();
	public List<String> getEnqStatus();
	
	public DashboardResponse getDashboardResponse(Integer userId);
	public boolean upsertEnquiry(EnquiryForm form);
	public List<StudentEnqEntity> getEnquiries();
	public List<StudentEnqEntity> getFilteredEnquiries(EnquirySearchCriteria searchCriteria);
	public EnquiryForm editEnquiry(Integer enquiryId);
	public boolean deleteEnquiry(Integer enquiryId);
	
}
