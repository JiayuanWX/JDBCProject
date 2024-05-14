package queries;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

public class JWangQueries {
	Connection conn = null;
	Statement st;
	ResultSet rs;
	PreparedStatement pst;
	Savepoint savepoint1 = null;
	
	public JWangQueries () {
	}
	
	public void openConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://dif-mysql:23306/DBI49", "DBI49", "DBI49");


		} catch (SQLException ex) {
			
			System.out.println("Connection Failed");
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public void closeConnection() {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
			if (pst != null)
				pst.close();

		} catch (SQLException ex) {

			System.out.println("Closing Connection Went Wrong!");
		}
	}
	
	public void query1() {
		try {
			openConnection();

			st = conn.createStatement();
			
            String query = "SELECT DISTINCT r.restaurname, r.city, r.rating " +
                    "FROM restaurant AS r " +
                    "WHERE r.restaurname IN (" +
                    "    SELECT s.restaurname " +
                    "    FROM serves AS s " +
                    "    JOIN dishes d ON s.dish = d.dish " +
                    "    WHERE d.dish = 'cheesecake' OR d.dish = 'pepperonipizza'" +
                    ") " +
                    "AND EXISTS (" +
                    "    SELECT s.restaurname " +
                    "    FROM sales AS s " +
                    "    WHERE r.restaurname = s.restaurname AND s.amount BETWEEN 10000 AND 20000" +
                    ") " +
                    "AND r.restaurname IN (" +
                    "    SELECT f.restaurname " +
                    "    FROM frequents f " +
                    "    JOIN person p ON f.nameId = p.nameId " +
                    "    WHERE p.nameId IN ('Hillary', 'Alexander')" +
                    ")";
			
			rs = st.executeQuery(query);
				
            while (rs.next()) {
                String restaurantName = rs.getString("restaurname");
                String city = rs.getString("city");
                double rating = rs.getDouble("rating");
                System.out.println("Restaurant: " + restaurantName + ", \t City: " + city + ", \t Rating: " + rating);
            }

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	}
	
	public void query2() {
		try {
			openConnection();
			
			st = conn.createStatement();
			
            String query = "SELECT e.Fname, e.Lname, e.Salary, COUNT(htc.HotelId) AS numHotels " +
                    "FROM employee AS e " +
                    "RIGHT JOIN employee_customer AS ec ON e.Ssn = ec.Emp_id " +
                    "LEFT JOIN hotel_trip_customer AS htc ON ec.Cust_Id = htc.CustomerId " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT * " +
                    "    FROM hotel_trip_customer AS htc2 " +
                    "    WHERE htc2.TripTo = 'Biarritz' AND htc2.CustomerId = htc.CustomerId" +
                    ") " +
                    "GROUP BY e.Fname, e.Lname, e.Salary " +
                    "HAVING e.Salary >= 30000 " +
                    "ORDER BY e.Salary ASC";
			
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                String firstName = rs.getString("Fname");
                String lastName = rs.getString("Lname");
                int salary = rs.getInt("Salary");
                int numHotels = rs.getInt("numHotels");

                System.out.println("Nombre: " + firstName + " " + lastName);
                System.out.println("Salario: " + salary);
                System.out.println("Número de hoteles: " + numHotels);
                System.out.println("--------------------------");
            }
            
		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	
	}
	
	public void query3() {
		try {
			openConnection();

			st = conn.createStatement();
			
            String query = "SELECT DISTINCT mg.genre " +
                    "FROM movies_genres mg " +
                    "WHERE NOT EXISTS (" +
                    "    SELECT d.id " +
                    "    FROM directors d " +
                    "    WHERE NOT EXISTS (" +
                    "        SELECT md.movie_id " +
                    "        FROM movies_directors md " +
                    "        WHERE md.director_id = d.id " +
                    "        AND md.movie_id IN (" +
                    "            SELECT mg2.movie_id " +
                    "            FROM movies_genres mg2 " +
                    "            WHERE mg2.genre = mg.genre" +
                    "        )" +
                    "    )" +
                    ")";
            
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                String genre = rs.getString("genre");
                System.out.println("Género de película: " + genre);
            }
            
		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	
	}
	
	public void insertTransaction() {
		try {
			openConnection();
			
			try {
				conn.setAutoCommit(false);
			}catch(SQLException e) {
				System.out.println("Something went wrong when disabling autocommit");
			}
			
			st = conn.createStatement();
			
			
			//show before SERVES and DISHES relations
			System.out.println("================= DISHES RELATION BEFORE INSERTING ================= \n");
			String beforeDishes = "SELECT * FROM dishes";
							
			rs = st.executeQuery(beforeDishes);
			
            while (rs.next()) {
                String dish = rs.getString("dish");
                String cuisine = rs.getString("cuisine");
                String category = rs.getString("category");
                Double difficulty = rs.getDouble("difficulty");
                		
                System.out.println("Dish name: " + dish + "\t\t\t cuisine: " + cuisine + "\t\t\t category: " + category +"\t\t\t difficulty: "+ difficulty +"\n");
            }
            
            
			System.out.println("================= SERVES RELATION BEFORE INSERTING ================= \n");
			String beforeServes = "SELECT * FROM serves";
							
			rs = st.executeQuery(beforeServes);
			
            while (rs.next()) {
                String restaurname = rs.getString("restaurname");
                String dish = rs.getString("dish");
                Double price = rs.getDouble("price");
                		
                System.out.println("Restaurant: " + restaurname + "\t\t serves: " + dish + "\t\t price: " + price + "\n");
            }
            
            
            try {//adding tortilla to Dish2Eat which does not exist in the DISHES table (foreign key constraint fails)
            	savepoint1 = conn.setSavepoint("savepoint1");
    			String insert1 = ("INSERT INTO serves (restaurname, dish, price) VALUES ('Dish2Eat', 'tortilla', '2.50')");
    			st.executeUpdate(insert1);
            	
            } catch (SQLException ex) {
    			if (ex.getMessage().contains("dish")) {
    				System.out.println("The dish isn't in dishes relation, so foreign constraint it's violated!!! Let's rollback.");
    				//enter = true;
    				
    				try {
    					conn.rollback(savepoint1);
    					System.out.println("Rolledback!\n");
    				} catch (SQLException e) {
    					System.out.println("Couldn't rollback to savepoint1!!");
    					e.printStackTrace();
    				}
    			}
            }
            
            
            System.out.println("Let's try again inserting first the tortilla dish to dishes relation");
			System.out.println("Inserting into dishes relation...");
			String update2 = ("INSERT INTO dishes (dish, cuisine, category, difficulty) VALUES ('tortilla', 'Spanish', 'Main Course', '2.00')");
			st.executeUpdate(update2);
			System.out.println("Tortilla inserted to dishes relation!!!\n\n");
			conn.commit();
			
			System.out.println("Trying again to insert serve in relation...");
			String update1 = ("INSERT INTO serves (restaurname, dish, price) VALUES ('Dish2Eat', 'tortilla', '2.50')");
			st.executeUpdate(update1);
			
			conn.commit();
			System.out.println("Tortilla from Dish2Eat inserted to serves relation!!!\n\n");

			

			//show after of relations
			System.out.println("================= DISHES RELATION AFTER INSERTING ================= \n");
			String afterDishes = "SELECT * FROM dishes";
							
			rs = st.executeQuery(afterDishes);
			
            while (rs.next()) {
                String dish = rs.getString("dish");
                String cuisine = rs.getString("cuisine");
                String category = rs.getString("category");
                Double difficulty = rs.getDouble("difficulty");
                		
                System.out.println("Dish name: " + dish + "\t\t\t cuisine: " + cuisine + "\t\t\t category: " + category +"\t\t\t difficulty: "+ difficulty +"\n");
            }
			
			
			System.out.println("================= SERVES RELATION AFTER INSERTING ================= \n");
			String afterServes = "SELECT * FROM serves";
							
			rs = st.executeQuery(afterServes);
			
            while (rs.next()) {
                String restaurname = rs.getString("restaurname");
                String dish = rs.getString("dish");
                Double price = rs.getDouble("price");
                		
                System.out.println("Restaurant: " + restaurname + "\t\t serves: " + dish + "\t\t price: " + price + "\n");
            }
			
           
            resetInsertTransData();
            
		} catch (SQLException ex) {
			System.out.println("Something went wrong!\n" + ex.getMessage());
		} finally {
			closeConnection();
		}
	
	}
	
	private void resetInsertTransData() {//just for it to do the same every ejecution
		try {
			openConnection();
			
			st = conn.createStatement();
			
			String deleteServes = "DELETE FROM serves WHERE restaurname = 'Dish2Eat' AND dish = 'tortilla'";
			st.executeUpdate(deleteServes);
			

			String deleteDish = "DELETE FROM dishes WHERE dish = 'tortilla'";	
			st.executeUpdate(deleteDish);
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}finally {
			closeConnection();
		}
	}
	
	
	
	public void updateTransaction() {// try to update English to Chinese(successfull) and French to Spanish (duplicated rollback)
		try {
			openConnection();
			
			try {
				conn.setAutoCommit(false);
			}catch(SQLException e) {
				System.out.println("Something went wrong when disabling autocommit");
			}
			
			st = conn.createStatement();
			
			
			//show before
			System.out.println("================= LANGUAGES SPOKEN BY GUIDE ID:72515633 BEFORE UPDATE ================= \n");
			String beforeLang = "SELECT Lang FROM languages WHERE GuideId = 72515633";
							
			rs = st.executeQuery(beforeLang);
			
            while (rs.next()) {
                String language = rs.getString("Lang"); 		
                System.out.println("Language: " + language);
            }
            
            
            System.out.println("\n\nLet's try changing from English to Chinese...");
    		String update1 = ("UPDATE languages SET Lang = 'Chinese' WHERE GuideId = '72515633' AND Lang = 'English'");
    		st.executeUpdate(update1);
            System.out.println("Succesfully updated language from English to Chinese!!\n\n");
            
            savepoint1 = conn.setSavepoint("savepoint1");
            
            try {
	            System.out.println("Let's try changing from French to Spanish...");
	    		String update2 = ("UPDATE languages SET Lang = 'Spanish' WHERE GuideId = '72515633' AND Lang = 'French'");
	    		st.executeUpdate(update2);
            }catch(SQLException ex){
    			if (ex.getMessage().contains("Duplicate")) {
    				System.out.println("The guide with ID:72515633 already speaks Spanish, let's rollback to savepoint.");
    			
    				try {
    					conn.rollback(savepoint1);
    					System.out.println("Rolledback!\n");
    				} catch (SQLException e) {
    					System.out.println("Couldn't rollback to savepoint1!!");
    					e.printStackTrace();
    				}
    			}
            }
            
			conn.commit();

			

			//show after
			System.out.println("================= LANGUAGES SPOKEN BY GUIDE ID:72515633 AFTER UPDATE ================= \n");
			String afterLang = "SELECT Lang FROM languages WHERE GuideId = 72515633";
							
			rs = st.executeQuery(afterLang);
			
            while (rs.next()) {
                String language = rs.getString("Lang"); 		
                System.out.println("Language: " + language);
            }
			
			resetUpdateTransData();
			
		} catch (SQLException ex) {
				System.out.println("Something went wrong!");
				ex.printStackTrace();
			
		} finally {

			closeConnection();
		}

	
	}
	
	private void resetUpdateTransData() {//just for it to do the same every ejecution
		try {
			openConnection();
			
			st = conn.createStatement();
			
			String restoreLanguage = "UPDATE languages SET Lang = 'English' WHERE GuideId = 72515633 AND Lang = 'Chinese'";
			st.executeUpdate(restoreLanguage);

		} catch (SQLException e) {
			e.printStackTrace();
			
		}finally {
			closeConnection();
		}
	}
	
	
	public void extraQuery1() {
		try {
			openConnection();

			st = conn.createStatement();
			
            String query = "SELECT htc.TripTo, htc.DepartureDate, SUM(htc.NumNights) AS totalNights " +
                    "FROM hotel_trip_customer AS htc " +
                    "GROUP BY htc.TripTo, htc.DepartureDate " +
                    "HAVING SUM(htc.NumNights) > (" +
                    "    SELECT AVG(bookedNights) " +
                    "    FROM (" +
                    "        SELECT SUM(NumNights) AS bookedNights " +
                    "        FROM hotel_trip_customer " +
                    "        GROUP BY TripTo, DepartureDate" +
                    "    ) AS nightsPerTrip" +
                    ")";
            
            while (rs.next()) {
                String tripTo = rs.getString("TripTo");
                Date departureDate = rs.getDate("DepartureDate");
                int totalNights = rs.getInt("totalNights");

                System.out.println("Destino del viaje: " + tripTo);
                System.out.println("Fecha de salida: " + departureDate);
                System.out.println("Total de noches reservadas: " + totalNights);
                System.out.println("--------------------------");
            }
            
            rs = st.executeQuery(query);
		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}

	}
	
	public void extraQuery2() {
		try {
			openConnection();

			st = conn.createStatement();
			
			   String query = "SELECT DISTINCT maxE.dish " +
                       "FROM eats AS maxE " +
                       "WHERE maxE.nameId = ( " +
                       "    SELECT e1.nameId " +
                       "    FROM eats AS e1 " +
                       "    GROUP BY e1.nameId " +
                       "    ORDER BY COUNT(e1.dish) DESC " +
                       "    LIMIT 1 " +
                       ") " +
                       "AND maxE.dish NOT IN ( " +
                       "    SELECT minE.dish " +
                       "    FROM eats minE " +
                       "    WHERE minE.nameId = ( " +
                       "        SELECT e2.nameId " +
                       "        FROM eats e2 " +
                       "        GROUP BY e2.nameId " +
                       "        ORDER BY COUNT(e2.dish) ASC " +
                       "        LIMIT 1 " +
                       "    ) " +
                       ")";
     
				rs = st.executeQuery(query);
     
			
			while(rs.next()) {
				String dish = rs.getString("dish");
                System.out.println(dish);			
                }

		} catch (SQLException ex) {
			
			System.out.println("Something went wrong!");
			
		} finally {

			closeConnection();

		}


	}
}
