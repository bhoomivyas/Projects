import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class ReadTransFile {

	public static void main(String[] args) {
		try{
			FileInputStream fileIn = new FileInputStream("transaction.dat");
			BufferedInputStream buffinStream = new BufferedInputStream(fileIn);
			DataInputStream in = new DataInputStream(buffinStream);
			
			
			System.out.println("Account Number: " + in.readInt());
			System.out.println("Cach Balance: " + in.readDouble());
			System.out.println("Account Value: " + in.readDouble());
			System.out.println("Total Account Value: " + in.readDouble());
			
			in.close();
		}catch(Exception ex){
			System.out.println("Exception: " + ex);
		}
	}

}

