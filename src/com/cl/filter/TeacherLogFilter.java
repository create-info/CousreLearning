package com.cl.filter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.cl.dao.BBS;
import com.cl.dao.Course;
import com.cl.dao.CourseStudent;
import com.cl.dao.CourseTeacher;
import com.cl.dao.Event;
import com.cl.dao.Homework;
import com.cl.dao.HomeworkStudent;
import com.cl.dao.KnowledgeWeight;
import com.cl.dao.LearningStatus;
import com.cl.dao.Log;
import com.cl.dao.Section;
import com.cl.dao.Student;
import com.cl.dao.Teacher;
import com.cl.util.DateFormator;
import com.cl.util.FileFunc;

public class TeacherLogFilter extends LogFilter implements Filter {
	private FilterConfig config;
	
	public void init(FilterConfig config) {
		this.config = config;
	}
	
	public void destroy() {
		this.config = null;
	}
	
	private Log createLog(Calendar nowtime, HttpServletRequest req) {
		Log log = new Log();
		log.setLog_id(0);

//		get loginname
		String loginname = null;
		if (req.getParameter("teacher.loginname") != null)
			loginname = req.getParameter("teacher.loginname");
		else if (req.getSession().getAttribute("username") != null)
			loginname = (String)req.getSession().getAttribute("username");
		
//		get teacher
		Teacher tea = Teacher.getInstance();
		if (loginname != null) {
			tea = Teacher.getTeacherByLoginname(loginname);
		}
		else {
			tea.setId(-1);
			tea.setName("null");
		}
		
		
		int course_id = INVAILD, class_id = INVAILD, section_id = INVAILD, event_id = INVAILD;
		if (req.getParameter("course_id") != null)
			course_id = new Integer(req.getParameter("course_id")).intValue();
		else if (req.getSession().getAttribute("course_id") != null)
			course_id = ((Integer)req.getSession().getAttribute("course_id")).intValue();
		if (req.getParameter("class_id") != null)
			class_id = new Integer(req.getParameter("class_id")).intValue();
		else if (req.getSession().getAttribute("class_id") != null)
			class_id = ((Integer)req.getSession().getAttribute("class_id")).intValue();
		else if (course_id != INVAILD)
			class_id = CourseTeacher.getCourseTeacherByCourseIdAndTeacherId(course_id, tea.getId()).getClass_id();
		if (req.getParameter("section_id") != null)
			section_id = new Integer(req.getParameter("section_id")).intValue();
		else if (req.getSession().getAttribute("section_id") != null)
			section_id = ((Integer)req.getSession().getAttribute("section_id")).intValue();
		if (req.getParameter("event_id") != null)
			event_id = new Integer(req.getParameter("event_id")).intValue();
		else if (req.getSession().getAttribute("event_id") != null)
			event_id = ((Integer)req.getSession().getAttribute("event_id")).intValue();
		
//		get course info
		Course course = Course.getInstance();
		if (course_id != INVAILD) {
			course = Course.getCourseByCourseId(course_id);
		}
		else {
			course.setCourse_id(course_id);
			course.setCourse_name("null");
			course.setCourse_classcapacity(-1);
		}
		
//		get section info
		Section section = Section.getInstance();
		section.setCourse_id(course_id);
		section.setSection_weight(INVAILD);
		if (section_id != INVAILD) {
			if (section_id != 0) {
				section = Section.getSectionBySectionId(section_id);
			}
			else {
				section.setSection_id(section_id);
				section.setSection_name("课程总结");
			}
		}
		else if (req.getParameter("section.section_name") != null) {
			section.setSection_id(INVAILD);
			section.setSection_name(req.getParameter("section.section_name"));
		}
		else {
			section.setSection_id(section_id);
			section.setSection_name("null");
		}
		
//		get event info
		Event event = Event.getInstance();
		if (event_id != INVAILD) {
			event = Event.getEventByEventId(event_id);
		}
		else if (req.getParameter("event.event_content") != null) {
			event.setEvent_content(req.getParameter("event.event_content"));
			event.setClass_id(class_id);
			event.setEvent_type(req.getParameter("event.event_type"));
		}
 		else {
			event.setClass_id(class_id);
			event.setEvent_content("null");
			event.setEvent_id(event_id);
			event.setEvent_type("null");
			event.setSection_id(section_id);
		}
		
//		get learningstatus
		LearningStatus ls = LearningStatus.getInstance();
		ls.setClass_id(class_id);
		ls.setEvent_id(event_id);
		if (req.getParameter("learningstatus.classtime") != null) {
			ls.setClasstime(new Integer(req.getParameter("learningstatus.classtime")));
			ls.setInclass(new Integer(req.getParameter("learningstatus.inclass")));
			ls.setOutclass(new Integer(req.getParameter("learningstatus.outclass")));
			ls.setMethod(req.getParameter("learningstatus.method"));
		}
		else {
			ls.setClasstime(-1);
			ls.setInclass(-1);
			ls.setOutclass(-1);
			ls.setMethod("null");
		}
		
//		get bbs
		BBS bbs = BBS.getInstance();
		bbs.setEvent_id(event_id);
		bbs.setClass_id(class_id);
		bbs.setEvent_id(event_id);
		bbs.setStudent_id(tea.getId());
		if (req.getParameter("reply_id") != null) {
			bbs.setReply_id(new Integer(req.getParameter("reply_id")));
			bbs.setBbs_content(req.getParameter("bbs.bbs_content"));
		}
		else {
			bbs.setReply_id(INVAILD);
			bbs.setBbs_content("null");
		}
		
//		get homework_id & student homework
		Homework hw = Homework.getInstance();
		if (req.getParameter("homework_id") != null) {
			hw = Homework.getHomeworkByHomeworkId(new Integer(req.getParameter("homework_id")));
			section_id = Homework.getHomeworkByHomeworkId(hw.getHomework_id()).getSection_id();
			section = Section.getSectionBySectionId(section_id);
		}
		else { 
			hw.setHomework_id(INVAILD);
			hw.setHomework_title("null");
			hw.setHomework_content("null");
			hw.setHomework_accessory("null");
		}
		
		HomeworkStudent hws = HomeworkStudent.getInstance();
		if (req.getParameter("homeworkstudent_id") != null) {
			hws = HomeworkStudent.getHomeworkStudentByHomeworkStudentId(new Integer(req.getParameter("homeworkstudent_id")));
			hw = Homework.getHomeworkByHomeworkId(hws.getHomework_id());
		}
		else {
			hws.setHomework_id(hw.getHomework_id());
		}
		
//		get knowledgeweight
		KnowledgeWeight kw = KnowledgeWeight.getInstance();
		kw.setSection_id(section_id);
		kw.setKnowledgeweight_id(INVAILD);
		kw.setListening_weight(INVAILD);
		kw.setAnswer_weight(INVAILD);
		kw.setAttendance_weight(INVAILD);
		kw.setHomework_weight(INVAILD);
		kw.setExperiment_weight(INVAILD);
		kw.setReviewandpreview_weight(INVAILD);
		
		log.setUser_id(tea.getId());
		log.setUser_name(tea.getName());
		log.setUser_gender("null");
		
		log.setAction("null");
		log.setTime(nowtime);
		
		log.setClass_id(class_id);
		log.setCourse(course);
		log.setSection(section);
		log.setEvent(event);
		
		log.setLs(ls);
		log.setBbs(bbs);
		
		log.setHw(hw);
		log.setHws(hws);
		log.setKw(kw);
		
		return log;
	}
	
	public void doFilter(ServletRequest req,
			ServletResponse res, FilterChain chain) 
			throws IOException, ServletException {
//		ServletContext context = this.config.getServletContext();
		HttpServletRequest hreq = (HttpServletRequest)req;
		String[] r = hreq.getRequestURI().split("/");
		String action = r[r.length - 1];
		String role = (String)hreq.getSession().getAttribute("role");
		Pattern p = Pattern.compile("^(teacher)_(.)*");
		
		if (p.matcher(action).matches() && (role == null || role.equals("teacher"))) {
			String subaction = getSubAction(action);
			System.out.println("##########Start##########");
			
			String datetime = (String) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			Calendar nowtime = DateFormator.getDateByPattern(datetime);
			req.setCharacterEncoding("utf-8");
			boolean status = true;
			Log log = Log.getInstance();
			if (req.getParameter("teacher.loginname") != null && req.getParameter("teacher.password") != null) {
				String loginname = req.getParameter("teacher.loginname");
				String password = req.getParameter("teacher.password");
				if (!Teacher.check(loginname, password))
					status = false;
			}
			if (status) {
				log = createLog(nowtime, hreq);
				if (log.getBbs().getReply_id() != -1 && log.getBbs().getReply_id() != INVAILD)
					subaction = "replybbs";
				else if (subaction.equals("getsectionsummary") && log.getSection().getSection_id() == 0)
					subaction = "getcoursesummary";
				
				boolean show = true;
				String propertiespath = (TeacherLogFilter.class.getResource("") + "teacherlog.properties").substring(5);
				String actioninchinese = FileFunc.readFromPropertiesFile(propertiespath, subaction);
				if (actioninchinese == "")
					show = false;
				else
					log.setAction(actioninchinese);
				
				
				System.out.println("action: " + action + " subaction: " + subaction);
				chain.doFilter(req, res);
				
				if (subaction.equals("updateknowledgeweight"))
					log.setKw(KnowledgeWeight.getKnowledgeWeightBySectionId(log.getSection().getSection_id()));
				
				if (show) {
					log.show();
					String path = hreq.getServletContext().getRealPath(savepath);
	//				System.out.println("path: " + path);
					if (!FileFunc.directoryExist(path))
						FileFunc.createDirectory(path);
	//				Log.saveLog(path, log, "teacher");
				}
				System.out.println("##########End##########\n");
			}
			else {
				chain.doFilter(req, res);
			}
		}
		else {
			chain.doFilter(req, res);
		}
	}
}
