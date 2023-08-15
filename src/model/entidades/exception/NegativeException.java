package model.entidades.exception;

public class NegativeException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public NegativeException(String msg) {
		super(msg);
	}
}
