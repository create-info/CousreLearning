package com.cl.dao;

import java.io.File;
import java.sql.Connection;
import java.util.Calendar;

import com.cl.dbquery.DBHelper;
import com.cl.util.DateFormator;

public class Log {
	private int log_id;
	private int user_id;
	private String user_name;
	private String user_gender;
	private String action;
	private Calendar time;
	private int class_id;
	private Course course;
	private Section section;
	private Event event;
	private LearningStatus ls;
	private BBS bbs;
	private Homework hw;
	private HomeworkStudent hws;
	private KnowledgeWeight kw;
	
	public int getLog_id() {
		return log_id;
	}

	public void setLog_id(int log_id) {
		this.log_id = log_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public LearningStatus getLs() {
		return ls;
	}

	public void setLs(LearningStatus ls) {
		this.ls = ls;
	}

	public BBS getBbs() {
		return bbs;
	}

	public void setBbs(BBS bbs) {
		this.bbs = bbs;
	}

	public Homework getHw() {
		return hw;
	}

	public void setHw(Homework hw) {
		this.hw = hw;
	}

	public HomeworkStudent getHws() {
		return hws;
	}

	public void setHws(HomeworkStudent hws) {
		this.hws = hws;
	}

	public KnowledgeWeight getKw() {
		return kw;
	}

	public void setKw(KnowledgeWeight kw) {
		this.kw = kw;
	}
	
	public String getLearingStatusString() {
		String res = "\nClasstime:\t" + LearningStatus.getClassTimeString(this.getLs().getClasstime())
					+ "\nInclass:\t" + LearningStatus.getClassString(this.getLs().getInclass())
					+ "\nOutclass:\t" + LearningStatus.getClassString(this.getLs().getOutclass())
					+ "\nMethod:\t" + this.getLs().getMethod();
		return res;		
	}

	@Override
	public String toString() {
		String log = "LogId:\t" + this.getLog_id() + "\nUserId:\t" + this.getUser_id() 
		+ "\nUsername:\t" + this.getUser_name() + "\nUsergender:\t" + this.getUser_gender() 
		+ "\nTime:\t" + DateFormator.getDateCalendarToString(this.getTime()) + "\nAction:\t" + this.getAction() 
		+ "\nCourseId:\t" + this.getCourse().getCourse_id() + "\nClassId:\t" + this.getClass_id() 
		+ "\nCourseName:\t"+ this.getCourse().getCourse_name() + "\nSectionId:\t" + this.getSection().getSection_id() 
		+ "\nSectionName:\t" + this.getSection().getSection_name() + "\nEventId:\t" + this.getEvent().getEvent_id() 
		+ "\nEventName:\t" + this.getEvent().getEvent_content() + "\nEventType:\t" + this.getEvent().getEvent_type()
		+ this.getLearingStatusString()
		+ "\nBBSContent:\t" + this.getBbs().getBbs_content() 
		+ "\nHomeworkId:\t" + this.getHw().getHomework_id() + "\nHomeworkTitle:\t" + this.getHw().getHomework_title() 
		+ "\nHomeworkContent:\t" + this.getHw().getHomework_content() 
		+ "\nHomeworkAccessory:\t" + this.getHw().getHomework_accessory()
		+ "\nHomeworkStudentComment:\t" + this.getHws().getHomeworkstudent_comment() 
		+ "\nHomeworkStudentAccessory:\t" + this.getHws().getHomeworkstudent_accessory() 
		+ "\nKnowledgeWeightId:\t" + this.getKw().getKnowledgeweight_id() + "\nListeningWeight:\t" + this.getKw().getListening_weight()
		+ "\nAnswerWeight:\t" + this.getKw().getAnswer_weight() + "\nAttendanceWeight:\t" + this.getKw().getAnswer_weight()
		+ "\nHomeworkWeight:\t" + this.getKw().getHomework_weight() + "\nExperimentWeight:\t" + this.getKw().getExperiment_weight()
		+ "\nReviewAndPreviewWeight:\t" + this.getKw().getReviewandpreview_weight() + "\n";
		return log;
	}
	
	public void show() {
		System.out.println(this.toString());
	}
	
	public static Log getInstance() {
		return new Log();
	}
	
	public static void saveLog(Log log, String role) {
		Connection con = DBHelper.getConnection();
		String sql = "insert into " + role + "log(user_id, user_name, user_gender, time, action, course_id, class_id, course_name, "
				+ "section_id, section_name, event_id, event_content, event_type, classtime, inclass, outclass, "
				+ "method, bbs_content, homework_id, homework_title, homework_content, homework_accessory, "
				+ "homeworkstudent_comment, homeworkstudent_accessory, knowledgeweight_id, listening_weight, "
				+ "answer_weight, attendance_weight, homework_weight, experiment_weight, reviewandpreview_weight) values("
				+ log.getUser_id() + ", '" + log.getUser_name() + "', '" + log.getUser_gender() + "', '" 
				+ DateFormator.getDateCalendarToString(log.getTime()) + "', '" + log.getAction() + "', " + log.getCourse().getCourse_id()
				+ ", " + log.getClass_id() + ", '" + log.getCourse().getCourse_name() + "', " + log.getSection().getSection_id()
				+ ", '" + log.getSection().getSection_name() + "', " + log.getEvent().getEvent_id() + ", '" + log.getEvent().getEvent_content()
				+ "', '" + log.getEvent().getEvent_type() + "', " + log.getLs().getClasstime() + ", " + log.getLs().getInclass() 
				+ ", " + log.getLs().getOutclass() + ", '" + log.getLs().getMethod() + "', '" + log.getBbs().getBbs_content() 
				+ "', " + log.getHw().getHomework_id() + ", '" + log.getHw().getHomework_title() + "', '" + log.getHw().getHomework_content() 
				+ "', '" + log.getHw().getHomework_accessory() + ", '" + log.getHws().getHomeworkstudent_comment() 
				+ "', '" + log.getHws().getHomeworkstudent_accessory() + "', " + log.getKw().getKnowledgeweight_id()
				+ ", " + log.getKw().getListening_weight() + ", " + log.getKw().getAnswer_weight() + ", " + log.getKw().getAttendance_weight()
				+ ", " + log.getKw().getHomework_weight() + ", " + log.getKw().getExperiment_weight() + ", " + log.getKw().getReviewandpreview_weight() + ");";
		DBHelper.execUpdate(con, sql);
		DBHelper.closeConnection(con);
	}
	
	public static String[] getLogList(String path) {
		File file = new File(path);
		return file.list();
	}
	
	public static void deleteLog(String filename) {
		File file = new File(filename);
		if (file.exists())
			file.delete();
	}
}
