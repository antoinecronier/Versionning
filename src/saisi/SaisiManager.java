package saisi;

import java.util.Scanner;

public class SaisiManager {

	public static synchronized String question(String message){
		String input = null;
        Scanner inputReader = new Scanner(System.in);
        System.out.println(message);
        input = inputReader.nextLine();
        return input;
	}
}
