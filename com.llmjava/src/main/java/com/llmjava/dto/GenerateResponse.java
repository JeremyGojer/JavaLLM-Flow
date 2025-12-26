package com.llmjava.dto;

//{"model":"llama3","created_at":"2025-12-10T19:20:12.3223969Z","response":"","done":true,"done_reason":"stop","context":[128006,882,128007,271,9906,2684,128009,128006,78191,128007,271,9906,1070,0,1102,596,6555,311,3449,499,13,2209,1070,2555,358,649,1520,499,449,11,477,1053,499,1093,311,6369,30],"total_duration":15991778400,"load_duration":9877836300,"prompt_eval_count":12,"prompt_eval_duration":1851256700,"eval_count":27,"eval_duration":4187093700}
public class GenerateResponse {

	private String model;
	private String created_at;
	private String response;
	private Boolean done;
	private String done_reason;
	private long [] context;
	private long total_duration;
	private long load_duration;
	private int prompt_eval_count;
	private long prompt_eval_duration;
	private int eval_count;
	private long eval_duration;
	private Message message;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Boolean getDone() {
		return done;
	}
	public void setDone(Boolean done) {
		this.done = done;
	}
	public String getDone_reason() {
		return done_reason;
	}
	public void setDone_reason(String done_reason) {
		this.done_reason = done_reason;
	}
	public long[] getContext() {
		return context;
	}
	public void setContext(long[] context) {
		this.context = context;
	}
	public long getTotal_duration() {
		return total_duration;
	}
	public void setTotal_duration(long total_duration) {
		this.total_duration = total_duration;
	}
	public long getLoad_duration() {
		return load_duration;
	}
	public void setLoad_duration(long load_duration) {
		this.load_duration = load_duration;
	}
	public int getPrompt_eval_count() {
		return prompt_eval_count;
	}
	public void setPrompt_eval_count(int prompt_eval_count) {
		this.prompt_eval_count = prompt_eval_count;
	}
	public long getPrompt_eval_duration() {
		return prompt_eval_duration;
	}
	public void setPrompt_eval_duration(long prompt_eval_duration) {
		this.prompt_eval_duration = prompt_eval_duration;
	}
	public int getEval_count() {
		return eval_count;
	}
	public void setEval_count(int eval_count) {
		this.eval_count = eval_count;
	}
	public long getEval_duration() {
		return eval_duration;
	}
	public void setEval_duration(long eval_duration) {
		this.eval_duration = eval_duration;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessages(Message message) {
		this.message = message;
	}
	
	
}
