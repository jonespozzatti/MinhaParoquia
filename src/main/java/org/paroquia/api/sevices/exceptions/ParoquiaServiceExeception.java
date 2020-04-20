package org.paroquia.api.sevices.exceptions;
public class ParoquiaServiceExeception extends Exception {
	
	private static final long serialVersionUID = -8113412686594169788L;
	private String msg;
    public ParoquiaServiceExeception(String msg){
     
      this.msg = msg;
    }
    
    public String getMessage(){
      return msg;
    }
	
}