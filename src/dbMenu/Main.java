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
		System.out.println("\n========== Queries made by Iñigo Vilá ==========\n");
		System.out.println("1. Return the department where its employees have the highest average of hours worked.");
		System.out.println("2. Return all pairs of customers that don't take part in any optional excursion.");
		System.out.println("3. Return the employees with the minimum average of salary per hour worked.");
		System.out.println("4. (EXTRA) For each language show the country with most population ordered by descending country population. \n");
		System.out.println("5. (EXTRA)  \n");
		System.out.println("6. TODO.");
		System.out.println("7. TODO.");
		System.out.println("\n========== Queries made by Jiayuan Wang ==========\n");
		System.out.println("8. ");
		System.out.println("9. ");
		System.out.println("10. ");
		System.out.println("11. (EXTRA)");
		System.out.println("12. (EXTRA)");
		System.out.println("13. TODO.");
		System.out.println("14. TODO.");
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
				//queriesJWang.updateTrasaction();
				break;
			
			default:
				System.out.println("Please, select a correct option.");
		}
	}

}