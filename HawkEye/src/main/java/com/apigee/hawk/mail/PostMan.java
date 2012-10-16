package com.apigee.hawk.mail;

public interface PostMan {
	public void sendMail(String emailSubject, String emailAddress, String emailPayload);

}
