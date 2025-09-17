package in.ankit.runner;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.ankit.entity.CourseEntity;
import in.ankit.entity.EnqStatusEntity;
import in.ankit.repo.CourseRepo;
import in.ankit.repo.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo enqStatusRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		courseRepo.deleteAll();
		CourseEntity c1 = new CourseEntity();
		c1.setCourseName("Java");
		CourseEntity c2 = new CourseEntity();
		c2.setCourseName("SBMS");
		CourseEntity c3 = new CourseEntity();
		c3.setCourseName("JRTP");
		
		List<CourseEntity> courseList = Arrays.asList(c1,c2,c3);
		courseRepo.saveAll(courseList);
		
		enqStatusRepo.deleteAll();
		EnqStatusEntity s1 = new EnqStatusEntity();
		s1.setStatusName("New");
		EnqStatusEntity s2 = new EnqStatusEntity();
		s2.setStatusName("Enrolled");
		EnqStatusEntity s3 = new EnqStatusEntity();
		s3.setStatusName("Lost");
		
		List<EnqStatusEntity> statusList = Arrays.asList(s1,s2,s3);
		enqStatusRepo.saveAll(statusList);
		
	}

}
