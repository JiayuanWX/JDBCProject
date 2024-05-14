package test;
import queries.*;

public class Test {

	public static void main(String[] args) {

					IVilaQueries queriesIVila = new IVilaQueries();
					JWangQueries queriesJWang = new JWangQueries();

					System.out.println("\n============================== Queries made by Iñigo Vilá ==============================\n");
					System.out.println("1. Retrieve the employee with the highest salary among the TWO employees who have the most female dependents and no male dependents.\n");
					queriesIVila.query1();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("2. For each city find the hotel most chosen among all customers, jointly with the number of times that it has been chosen\n");
					queriesIVila.query2();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("3. Return the customer ID's, name and count of different trip destinations of those customers that have gone on at least\n"
							+ "the same number of trips than the employee that works the least hours. \n");
					queriesIVila.query3();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("4. (EXTRA) For each language show the country with most population ordered by descending country population. \n");
					queriesIVila.extraQuery1();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("5. (EXTRA)\n");
					//queriesIVila.extraQuery2();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("6.  \n");
					//queriesIVila.insertTrasaction();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("6.  \n");
					//queriesIVila.insertTrasaction();
					
					System.out.println("\n============================== Queries made by Jiayuan Wang ==============================\n");
					System.out.println("==========================================================================================================================================\n");
					System.out.println("8. Retrieve the rating of the restaurants that serve ‘cheesecake’ or ‘pepperonipizza’ which has at least one daily sale of an amount greater than 10000 but less than 20000. \n");
					queriesJWang.query1();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("9. Retrieve the Fname, Lname, salary, and nº of hotels visited, for employees who are also customers, have not visited 'Biarritz' and earn more than 30000, sorted by salary. \n");
					queriesJWang.query2();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("10. List the genres where ALL the directors have directed at least a movie of that genre.\n");
					queriesJWang.query3();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("11. (EXTRA) Retrieve the TripTo, DepartureDate, and totalNights of the trips that across all customers have more totalNights than the average. \n");
					queriesJWang.extraQuery1();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("12. (EXTRA) Print the difference of dishesnames between the person that has eaten the most dishes and the least. \n");
					//queriesJWang.extraQuery2();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("13. INSERT INTO serves (restaurname, dish, price) VALUES ('Dish2Eat', 'tortilla', '2.50')\n");
					//queriesJWang.insertTrasaction();
					
					System.out.println("==========================================================================================================================================\n");
					System.out.println("14. Try to update English to Chinese and French to Spanish\n");
					//queriesJWang.insertTrasaction();
				}
		
}