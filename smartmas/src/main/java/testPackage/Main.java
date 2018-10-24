package testPackage;
import jade.Boot;

public class Main {
	public static void main(String[] args) {
		String[] parameters = new String[] {
				//"-container", 
				//"-host", "10.77.70.178",
				"-port", "1099",
				"-local-port", "1111"
				,"test:testPackage.TrackRunnerAgent;"};
		
		try{
			Boot.main(parameters);
		}
		catch (Exception e){
			System.out.print(e.getMessage());
		}
	}
	
}
