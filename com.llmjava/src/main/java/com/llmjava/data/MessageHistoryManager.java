package com.llmjava.data;

import java.util.ArrayList;
import java.util.List;

import com.llmjava.dto.Message;

public class MessageHistoryManager {

	private Message systemMessage;
	private List<Message> messageHistory;
	private int messageCapacity;
	
	public MessageHistoryManager() {
		if(messageHistory==null) {
			messageHistory = new ArrayList<>();
		}
		messageCapacity = 30;//TODO : Use from configuration
	}
	
	public MessageHistoryManager(String systemInstruction) {
		this();
		systemMessage = new Message("system", systemInstruction);
		messageHistory.add(systemMessage);
	}
	
	public MessageHistoryManager(int messageCapacity) {
		this();
		this.messageCapacity = messageCapacity;
	}
	
	public MessageHistoryManager(int messageCapacity, String systemInstruction) {
		this(systemInstruction);
		this.messageCapacity = messageCapacity;
	}
	
	public List<Message> getMessageHistory(){
		return messageHistory;
	}
	
	public List<Message> append(Message message){
		while(messageHistory.size() >= messageCapacity) {
			messageHistory.remove(1);
		}
		messageHistory.add(message);
		return messageHistory;
	}
	
}
