package org.citizensense.controller;


import java.util.Collection;

import org.citizensense.model.Answer;
import org.citizensense.model.Submission;
import org.citizensense.util.*;

import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

public class SubmissionController implements ModelDriven<Object>{
	

	private Collection<Submission> list;
	private int id;
	private Submission task = new Submission();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	@Override
	public Object getModel() {
		return (list != null ? list : task);
	}
	
	public HttpHeaders show() {
		return new DefaultHttpHeaders("show");
	}
	
	public void setId(String id) {
		if (id != null)
			for(Submission s : SubmissionService.getSubmissions()) {
				if(s.getId() == Integer.parseInt(id))
					this.task = s;
			}
//			this.task = SubmissionService.getSubmissions()..get(Integer.parseInt(id)-1);
		this.id = Integer.parseInt(id);		
	}
	
	public int getId() {
		return this.id;
	}
	
	public HttpHeaders index() {
		list = SubmissionService.getSubmissions();
		return new DefaultHttpHeaders("index").disableCaching();
	}
	
	public HttpHeaders create()
	{
		SubmissionService.save(task);
		return new DefaultHttpHeaders("create");
	}

}