package in.ankit.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ankit.binding.DashboardResponse;
import in.ankit.binding.EnquiryForm;
import in.ankit.binding.EnquirySearchCriteria;
import in.ankit.entity.CourseEntity;
import in.ankit.entity.EnqStatusEntity;
import in.ankit.entity.StudentEnqEntity;
import in.ankit.entity.UserDtlsEntity;
import in.ankit.repo.CourseRepo;
import in.ankit.repo.EnqStatusRepo;
import in.ankit.repo.StudentEnqRepo;
import in.ankit.repo.UserDtlsRepo;
import jakarta.servlet.http.HttpSession;

@Service
public class EnquiryServiceImpl implements EnquiryService {
	
	@Autowired
	private UserDtlsRepo userRepo;
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;
	
	@Autowired
	private StudentEnqRepo studentRepo;
	
	@Autowired
	private HttpSession session;

	@Override
	public DashboardResponse getDashboardResponse(Integer userId) {
		DashboardResponse response = new DashboardResponse();
		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		if(findById.isPresent()){
			List<StudentEnqEntity> enquiries = findById.get().getEnquiries();
			Integer totalEnq = enquiries.size();			
			Integer enrolledEnd = enquiries.stream().filter(e -> e.getEnquiryStatus().equals("Enrolled")).collect(Collectors.toList()).size();
			Integer lostEnq = enquiries.stream().filter(e -> e.getEnquiryStatus().equals("Lost")).collect(Collectors.toList()).size();
			
			
			response.setTotalEnq(totalEnq);
			response.setEnrolledEnq(enrolledEnd);
			response.setLostEnq(lostEnq);	
		}
		return response;
	}
	
	@Override
	public List<String> getCourses() {
		List<CourseEntity> courseEnity = courseRepo.findAll();
		List<String> courseList = new ArrayList<>();
		for(CourseEntity entity :courseEnity) {
			courseList.add(entity.getCourseName());
		}
		return courseList;
	}

	@Override
	public List<String> getEnqStatus() {
		List<EnqStatusEntity> statusEntity = enqStatusRepo.findAll();
		List<String> statusList = new ArrayList<>();
		for(EnqStatusEntity entity :statusEntity) {
				statusList.add(entity.getStatusName());	
		}
		return statusList;

	}

	
	@Override
	public boolean upsertEnquiry(EnquiryForm form) {
		StudentEnqEntity enqEntity = new StudentEnqEntity();
		BeanUtils.copyProperties(form, enqEntity);
		
		Integer userId = (Integer) session.getAttribute("userId");
		UserDtlsEntity userEntity = userRepo.findById(userId).get(); // why get()???
		
		enqEntity.setUser(userEntity);
		studentRepo.save(enqEntity);
		return true;
	}


	@Override
	public List<StudentEnqEntity> getEnquiries() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		if(findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			return enquiries;
		}
		
		return null;
	}

	@Override
	public List<StudentEnqEntity> getFilteredEnquiries(EnquirySearchCriteria criteria) {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserDtlsEntity> findById = userRepo.findById(userId);
		if(findById.isPresent()) {
			UserDtlsEntity userDtlsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDtlsEntity.getEnquiries();
			
			if(null!=criteria.getCourseName() && !"".equals(criteria.getCourseName())) {
				enquiries = enquiries.stream().filter(e -> e.getCourseName().equals(criteria.getCourseName())).collect(Collectors.toList());
			}
			if(null!=criteria.getEnquiryStatus() && !"".equals(criteria.getEnquiryStatus())) {
				enquiries = enquiries.stream().filter(e->e.getEnquiryStatus().equals(criteria.getEnquiryStatus())).collect(Collectors.toList());
			}
			if(null!=criteria.getClassMode() && !"".equals(criteria.getClassMode())) {
				enquiries = enquiries.stream().filter(e->e.getClassMode().equals(criteria.getClassMode())).collect(Collectors.toList());
			}
			return enquiries;	
		}
		return null;
	}
	
	
	
	@Override
	public EnquiryForm editEnquiry(Integer enquiry_id) {
		return null;
	}

	@Override
	public boolean deleteEnquiry(Integer enquiryId) {
		studentRepo.deleteById(enquiryId);
		return true;
	}

	


}
