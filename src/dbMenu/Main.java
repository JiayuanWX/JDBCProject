package dbMenu;

import java.util.Scanner;

import queries.*;
public class Main {
	
	static IVilaQueries queriesIVila;
	static JWangQueries queriesJWang;
	
	static Scanner scanner;

	public static void main(String[] args) {
		
		queriesIVila = new IVilaQueries();
		
		queriesJWang = new JWangQueries();

		
		scanner = new Scanner(System.in);
		
		int option = 1;
		while(option != 0) {
			printMenu();
			option = scanner.nextInt();
			checkOption(option);
		}
		
		scanner.close();
	}
	
	public static void printMenu() {
		System.out.println("\n\n\n\n\n=================================================================================================================================\n");

		System.out.println("\n========== Queries made by Iñigo Vilá ==========\n");
		System.out.println("1. Return the department where its employees have the highest average of hours worked.");
		System.out.println("2. Return all pairs of customers that don't take part in any optional excursion.");
		System.out.println("3. Return the employees with the minimum average of salary per hour worked.");
		System.out.println("4. (EXTRA) For each language show the country with most population ordered by descending country population. \n");
		System.out.println("5. (EXTRA)  \n");
		System.out.println("6. TODO.");
		System.out.println("7. TODO.");
		System.out.println("\n========== Queries made by Jiayuan Wang ==========\n");
		System.out.println("8. Retrieve the rating of the restaurants that serve ‘cheesecake’ or ‘pepperonipizza’ which has at least one daily sale of an amount greater than 10000 but less than 20000.");
		System.out.println("9. Retrieve the Fname, Lname, salary, and nº of hotels visited, for employees who are also customers, have not visited 'Biarritz' and earn more than 30000, sorted by salary.");
		System.out.println("10. List the genres where ALL the directors have directed at least a movie of that genre.");
		System.out.println("11. (EXTRA) Retrieve the TripTo, DepartureDate, and totalNights of the trips that across all customers have more totalNights than the average.");
		System.out.println("12. (EXTRA) Print the difference of dishesnames between the person that has eaten the most dishes and the least.");
		System.out.println("13. Try to INSERT INTO serves (restaurname, dish, price) VALUES ('Dish2Eat', 'tortilla', '2.50')");
		System.out.println("14. Try to update English to Chinese and French to Spanish\n"
				+ "		try {");
		System.out.println("0. Exit.");
		System.out.println("\nWrite your choice: ");
	}
	
	public static void checkOption(int option) {
		switch (option) {
			case 0:
				System.out.println("Gud, bye!");
				break;
			case 1:
				queriesIVila.query1();
				break;
			case 2:
				queriesIVila.query2();
				break;
			case 3:
				queriesIVila.query3();
				break;
			case 4:
				queriesIVila.extraQuery1();
				break;
			case 5:
				//queriesIVila.extraQuery2();
				break;
			case 6:
				//queriesVila.insertTransaction();
				break;
			case 7:
				//queriesVila.updateTrasaction();
				break;
			case 8:
				queriesJWang.query1();
				break;
			case 9:
				queriesJWang.query2();
				break;
			case 10:
				queriesJWang.query3();
				break;
			case 11:
				queriesJWang.extraQuery1();
				break;
			case 12:
				queriesJWang.extraQuery2();
				break;
			case 13:
				queriesJWang.insertTransaction();
				break;
			case 14:
				queriesJWang.updateTransaction();
				break;
			
			default:
				System.out.println("Please, select a correct option.");
		}
	}

}