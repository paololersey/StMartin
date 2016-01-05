package org.springframework.samples.hibernate.beans;

import java.util.List;

public class GlobalPdf {

	private List<Activity> activityList;

	private List<String> noteList;
	
	private List<Person>  peopleList;
	
	private ProjectPerson  projectPerson;

	private Filter filter;

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public List<String> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<String> noteList) {
		this.noteList = noteList;
	}

	public List<Person> getPeopleList() {
		return peopleList;
	}

	public void setPeopleList(List<Person> peopleList) {
		this.peopleList = peopleList;
	}

	public ProjectPerson getProjectPerson() {
		return projectPerson;
	}

	public void setProjectPerson(ProjectPerson projectPerson) {
		this.projectPerson = projectPerson;
	}

}
