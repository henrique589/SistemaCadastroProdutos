package model.exceptions;

public class NegativeException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public NegativeException(String msg) {
		super(msg);
	}
}
