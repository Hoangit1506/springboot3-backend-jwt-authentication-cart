package com.example.springboot3_backend_jwt_authentication_cart.payload.response;

public class MessageResponse {
	private String message;

	  public MessageResponse(String message) {
	    this.message = message;
	  }

	  public String getMessage() {
	    return message;
	  }

	  public void setMessage(String message) {
	    this.message = message;
	  }
}
