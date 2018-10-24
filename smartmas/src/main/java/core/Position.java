package core;

import java.io.Serializable;

public class Position implements Serializable {
	
	private String _color;
	
	public Position(String color){
		_color = color;
	}
	
	public String getColor(){
		return _color;
	}
	
	private static final long serialVersionUID = 5816805347153538753L;
}
