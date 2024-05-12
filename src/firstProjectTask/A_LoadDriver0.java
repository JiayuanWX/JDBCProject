package firstProjectTask;

public class A_LoadDriver0 {
	public static void main(String[] args) {
		/*
		 * You should have added the *downloaded* driver to your classpath -> Project ->
		 * Properties -> Libraries -> Classpath -> External JAR -> select the folder
		 * where the .jar is
		 * 
		 * or, if you have copied the file into the lib folder of your project -> Add
		 * JAR
		 */

		System.out.println("----- MySQL JDBC Connection Testing -> Load Driver -----");

		try {
			// The newInstance() call is a work around for some broken Java implementations
			Class.forName("com.mysql.cj.jdbc.Driver"); // .newInstance();
			System.out.println("--> MySQL JDBC Driver Registered!");
		} catch (Exception ex) {
			// handle the error
			System.out.println("ooops  --> Where is the MySQL JDBC Driver?");
			ex.printStackTrace();
//			return;
		}

	}
}
