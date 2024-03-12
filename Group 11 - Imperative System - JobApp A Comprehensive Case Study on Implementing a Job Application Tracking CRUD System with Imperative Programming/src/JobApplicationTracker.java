import java.util.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
public class JobApplicationTracker {
	static Connection con; // JDBC connection object
	static Statement stmt; // SQL statement object for executing queries
	static ResultSet rs; // Result set object for storing query results
	static String query; // String variable to store SQL queries
	//Applicant Status
	static String status[] = {"In Review", "Interview Scheduled", "Rejected", "Hired", "Custom"};
	static Scanner sc = new Scanner(System.in);
	public static void main(String[]args){
		
		//Data Types
		int choice = 0;
		//Program Menu
		String menu[] = {"Add", "View", "Update", "Delete", "Exit"};
		System.out.println("\u001B[36m|----------- JOB APPLICATION TRACKER -----------|\u001B[37m");
			while(true) {
				System.out.println("\nMenu:"
						+ "\n\n1 - "+menu[0]
						+ "\n2 - "+menu[1]
						+ "\n3 - "+menu[2]
						+ "\n4 - "+menu[3]
						+ "\n5 - "+menu[4]);
				System.out.print("\nSelect: ");
				choice = sc.nextInt();
				switch (choice) {
	
					//CREATE
					case 1:
						AddApplicant();
						break;
					//READ
					case 2:
						ViewApplicant();
						break;
						
					//UPDATE
					case 3:
						while(true) {
							UpdateApplicant();
							System.out.println("\n\t\tUpdate Again: \n\n\t\t1 - Yes\n\t\t2 - No");
							System.out.print("\n\t\tSelect: ");
							choice = sc.nextInt();
							if (choice == 2) {
								System.out.println("\n\t\u001B[35m|--------------------------------------|\u001B[37m");
								break;
							}
						}
						break;
					//DELETE
					case 4:
						while(true) {
							DeleteApplicant(); 
							System.out.println("\n\t\tDelete Again: \n\n\t\t1 - Yes\n\t\t2 - No");
							System.out.print("\n\t\tSelect: ");
							choice = sc.nextInt();
							if (choice == 2) {
								System.out.println("\n\t\u001B[31m|--------------------------------------|\u001B[37m");
								break;
							}
						}
						break;
					case 5:
						System.out.println("\n\u001B[36m|-----------------------------------------------|\u001B[37m");
						return;
					default:
						System.out.println("\nError: Invalid Input");
						break;
				}
				System.out.printf("\nDo you wish to continue? \n\n1 - yes\n2 - no");
				System.out.print("\n\nSelect: ");
				
				choice = sc.nextInt();
				if(choice == 1) {
					
				}else if(choice == 2) {
					break;
				}else {
					System.out.println("\nError: Invalid Input");
					break;
				}
			}
		System.out.println("\n\u001B[36m|-----------------------------------------------|\u001B[37m");
	}
	/**
     * Establish SQL connection and initialize necessary variables for executing SQL queries
     */
	public static Connection GetCon(){
    	Connection con = null;
        String dbUrl = "jdbc:sqlserver://GYRRO\\SQLEXPRESS;databaseName=JobApplicationDB;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
        try {
        	con = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            System.out.println("Error occurred while connecting to the database: " + e.getMessage());
        }
        return con;
    }
	
	/**
     * Adds a new applicant to the system.
     */
    public static void AddApplicant() {
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		java.sql.Date applyDate = java.sql.Date.valueOf(currentDate);
		java.sql.Date interviewDate = null;
    	String appInfo[] = new String[7];
		/* 0 applicant_first_name
		 * 1 applicant_last_name
		 * 2 phone_number
		 * 3 email_address
		 * 3 position
		 * 4 status
		 * 5 applied_date
		 * 6 interview_date
		*/
    	appInfo[6] = null;
    	System.out.println("\n\t\u001B[33m|------- Enter applicant details -------|\u001B[37m");
		sc.nextLine();
		
		System.out.print("\n\tFirst name: ");
		appInfo[0] = sc.nextLine();
		
		System.out.print("\n\tLast name: ");
		appInfo[1] = sc.nextLine();
		
		System.out.println("\n\tContact info:");
		System.out.print("\n\t\tPhone number: ");
		appInfo[2] = sc.nextLine();
		if(!appInfo[2].matches("\\d+")) {
			System.out.println("\n\tError: Input phone number is incorrect.");
			System.out.println("\n\t\u001B[33m|---------------------------------------|\u001B[37m");
			return;
		}
		
		System.out.print("\n\t\tEmail address: ");
		appInfo[3] = sc.nextLine();
		if(!appInfo[3].contains("@")) {
			System.out.println("\n\tError: Input email address is incorrect.");
			System.out.println("\n\t\u001B[33m|---------------------------------------|\u001B[37m");
			return;
		}
		
		System.out.print("\n\tPosition to apply: ");
		appInfo[4] = sc.nextLine();
		
		System.out.println("\n\tCurrent status:"
				+"\n\n\t\t1 - "+status[0]
				+"\n\t\t2 - "+status[1]
				+"\n\t\t3 - "+status[2]
				+"\n\t\t4 - "+status[3]
				+"\n\t\t5 - "+status[4]);
		System.out.print("\n\t\tSelect: ");
		
		int choice = sc.nextInt();
		if ((choice >= 1) && (choice <= 5)) {
		    if (choice == 5) {
		        System.out.print("\n\tEnter custom status: ");
		        appInfo[5] = sc.next();
		    } else if (choice == 2) {
		        System.out.print("\n\tEnter interview date (yyyy-mm-dd): ");
		        appInfo[5] = status[1];
		        appInfo[6] = sc.next();
		    } else {
		        appInfo[5] = status[choice - 1];
		    }
		} else {
		    System.out.println("\n\tError: Invalid Input");
		}

		try {
		    if (appInfo[6] == null) {
		    	interviewDate = null;
		    }else {
		    	interviewDate = java.sql.Date.valueOf(appInfo[6]);
		    }
	        long start = System.currentTimeMillis();
	        
			try {
			    con = GetCon();
			    stmt = con.createStatement();
			    
			    query = "INSERT INTO tbl_applicant (applicant_first_name,"
			            + " applicant_last_name,"
			            + " phone_number,"
			            + " email_address,"
			            + " position,"
			            + " application_status,"
			            + " applied_date,"
			            + " interview_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			    PreparedStatement pstmt = con.prepareStatement(query);

			    pstmt.setString(1, appInfo[0]); // First name
			    pstmt.setString(2, appInfo[1]); // Last name
			    pstmt.setString(3, appInfo[2]); // Phone number
			    pstmt.setString(4, appInfo[3]); // Email address
			    pstmt.setString(5, appInfo[4]); // Position
			    pstmt.setString(6, appInfo[5]); // Application status
			    pstmt.setDate(7, applyDate); // Date applied
			    pstmt.setDate(8, interviewDate); // Interview date

			    pstmt.executeUpdate();
			    con.close();
			    System.out.println("\n\tApplicant added successfully");
			} catch (SQLException e) {
			    System.out.println("Error: Failed to insert data: " + e.getMessage());
			}
			long end = System.currentTimeMillis();
			long elapsedTime = end-start;
		    System.out.printf("\n\tExecution time: (%.3f sec)\n", elapsedTime/1000.0);
		    
		} catch (IllegalArgumentException e) {
		    System.out.println("\n\tError: Input date format is incorrect. Please use the format 'yyyy-MM-dd'."+e);
		}
		System.out.println("\n\t\u001B[33m|---------------------------------------|\u001B[37m");
    }
    
    /**
     * Views details of applicants.
     */
    private static void ViewApplicant(){
    	System.out.println("\n\t\u001B[32m|------- View applicant details -------|\u001B[37m");
		System.out.println("\n\t1 - View all"
				+ "\n\t2 - View in review applicants"
				+ "\n\t3 - View interview scheduled applicants"
				+ "\n\t4 - View rejected applicants"
				+ "\n\t5 - View hired applicants"
				+ "\n\t6 - Search");
		System.out.print("\n\tSelect: ");
		int viewType = sc.nextInt();
		
		
	    //Select if view all or search
	    if(viewType == 1) {
	    	query = "SELECT * FROM tbl_applicant";
	    }else if(viewType==2) {
	    	query = "SELECT * FROM tbl_applicant WHERE application_status = 'In Review'";
	    }else if(viewType==3) {
	    	query = "SELECT * FROM tbl_applicant WHERE application_status = 'Interview Scheduled'";
	    }else if(viewType==4) {
	    	query = "SELECT * FROM tbl_applicant WHERE application_status = 'Rejected'";
	    }else if(viewType==5) {
	    	query = "SELECT * FROM tbl_applicant WHERE application_status = 'Hired'";
	    }else if(viewType==6){
	    	sc.nextLine();
			System.out.print("\n\tSearch: ");
			String search = sc.nextLine();
			
			//Search by int
			if(search.matches("\\d+")) {
				int id = Integer.parseInt(search);
				query = "SELECT * FROM tbl_applicant WHERE ID = " + id ;
			//Search by date
			}else if(search.matches(".*\\d+.*") && search.contains("-")) {
				java.sql.Date date = java.sql.Date.valueOf(search);
				query = "SELECT * FROM tbl_applicant WHERE '"+date+"' in (applied_date, interview_date)";
			//Search by String
			}else {
		    	query = "SELECT * FROM tbl_applicant WHERE '"+search+"' in (applicant_first_name,"
		    			+ " applicant_last_name,"
		    			+ " phone_number, "
		    			+ " email_address,"
		    			+ " position,"
		    			+ " application_status)";
			}
	    }else {
			System.out.println("\n\tError: Invalid Input");
			return;
	    }
	    System.out.print("\n\tID\t| "
				+ "First name\t| "
				+ "Last name\t| "
				+ "Phone number\t| "
				+ "Email address\t\t\t| "
				+ "Position\t\t| "
				+ "Status\t\t|"
				+ "Date Applied\t| "
				+ "Interview Date|\n\n");
		
	    //Viewing of applicant from database 
		long start = System.currentTimeMillis();
		try {
			con = GetCon();
		    stmt = con.createStatement();
		    rs = stmt.executeQuery(query);
		    if(!rs.isBeforeFirst()) {
			    System.out.println("\n\tError: Applicant not found!\n");
		    }else {
		    	while(rs.next()) {
		    		System.out.printf("\t%d\t| %-12s\t| %-11s\t| %-13s\t| %-25s\t| %-15s\t| %-20s\t| %-10s\t| %-10s\t|\n",
			    	rs.getInt("ID"),
			    	rs.getString("applicant_first_name"),
			    	rs.getString("applicant_last_name"),
			    	rs.getString("phone_number"),
			    	rs.getString("email_address"),
			    	rs.getString("position"),
			    	rs.getString("application_status"),
			    	rs.getString("applied_date"),
			    	rs.getString("interview_date"));
			    }
			    con.close();
		    }
		}catch (Exception e) {
			System.out.println("Error executing statement: " + e.getMessage());
	    }
	    long end = System.currentTimeMillis();
	    long elapsedTime = end-start;
	    //Prints execution time
		System.out.printf("\n\tExecution time: (%.3f sec)\n", elapsedTime/1000.0);
		System.out.println("\n\t\u001B[32m|--------------------------------------|\u001B[37m");
	}

    /**
     * Updates an existing applicant's information.
     */ 
    private static void UpdateApplicant(){

    	String value = "";
		System.out.println("\n\t\u001B[35m|---------- Update applicant  ----------|\u001B[37m");
		
		System.out.print("\n\tEnter ID: ");
		int updateID = sc.nextInt();
		sc.nextLine();
		
		System.out.println("\n\tEnter column to update: ");
		System.out.println("\n\t\t1 - First name"
				+ "\n\t\t2 - Last name"
				+ "\n\t\t3 - Phone number"
				+ "\n\t\t4 - Email address (johnDoe@email.com)"
				+ "\n\t\t5 - Position"
				+ "\n\t\t6 - Status"
				+ "\n\t\t7 - Interview Date (yyyy-mm-dd");
		System.out.print("\n\tSelect: ");
		int column = sc.nextInt();
		sc.nextLine();
		
		if(column==5) {
			System.out.println("\n\tCurrent status:"
					+"\n\n\t\t1 - "+status[0]
					+"\n\t\t2 - "+status[1]
					+"\n\t\t3 - "+status[2]
					+"\n\t\t4 - "+status[3]
					+"\n\t\t5 - "+status[4]);
			System.out.print("\n\t\tSelect: ");
			int choice = sc.nextInt();
			for(int i = 0; i<5; i++) {
				if(choice == i) {
					value = status[i-1];
				}else if(choice == 5) {
					System.out.print("\n\tEnter custom status:");
					value = sc.next();
					break;
				}
			}
		}else if ((column > 0) && (column < 8)){
			System.out.print("\n\tEnter new values: ");
			value = sc.nextLine();
			if(column == 3) {
				if(!value.matches("\\d+")) {
					System.out.println("\n\tError: Input phone number is incorrect.");
					System.out.println("\n\t\u001B[35m|---------------------------------------|\u001B[37m");
					return;
				}
			}else if(column == 4) {
				if(!value.contains("@")) {
					System.out.println("\n\tError: Input email address is incorrect.");
					System.out.println("\n\t\u001B[35m|---------------------------------------|\u001B[37m");
					return;
				}
			}
		}
		else {
			System.out.println("\n\tError: Invalid input");
			return;
		}
		String columnDB = "";
		String appColDB[] = {"applicant_first_name", "applicant_last_name", "phone_number", "email_address", "position", "application_status", "interview_date"};
		for(int i = 0; i < appColDB.length; i ++) {
			if(column-1 == i) {
				columnDB = appColDB[i];
			}
		}
		//SQL process
		long start = System.currentTimeMillis();
		try {
			con = GetCon();
			stmt = con.createStatement();
		    query = "UPDATE tbl_applicant SET "+columnDB+" = '" + value + "' WHERE ID = " + updateID;

		    //Check if rows are affected
		    int rowsAffected = stmt.executeUpdate(query);
		    if (rowsAffected > 0) {
		        System.out.println("\n\tApplicant "+updateID+" updated successfully.");
		    }else
		    	System.out.println("\n\tUpdate failed. Check if the ID exists or the inputted values and columns are correct.");
		    con.close();
		} catch (SQLException e) {
			System.out.println("\n\tError: Invalid input value");
		}
	    long end = System.currentTimeMillis();
	    long elapsedTime = end-start;
    	System.out.printf("\n\tExecution time: (%.3f sec)\n", elapsedTime/1000.0);
    }
    
    /**
     * Deletes an applicant from the system.
     */
	public static void DeleteApplicant(){
		System.out.println("\n\"\u001B[31m|---------- Delete applicant  ----------|\u001B[37m");
		
		System.out.print("\n\tEnter applicant ID: ");
		int deleteID = sc.nextInt();
		
		System.out.println("\n\tConfirm deletion of Applicant: "+deleteID+"?");
		System.out.println("\n\t\t1 - yes\n\t\t2 - no");
		System.out.print("\n\tSelect: ");
		int choice = sc.nextInt();

		//Deletion of applicant in database
		long start = System.currentTimeMillis();
		if(choice == 1) {
			try {
				con = GetCon();
			    stmt = con.createStatement();
			    query = "DELETE FROM tbl_applicant WHERE ID = "+deleteID;
			    
			    int rowsAffected = stmt.executeUpdate(query);
			    if (rowsAffected > 0) 
			    	System.out.println("\n\tApplicant "+deleteID+" deleted successfully.");
			    else {
			    	System.out.println("\n\tNo applicant deleted. Check if the ID exists.");
			    }
			    
			    con.close();
			}catch (Exception e) {
				System.out.println("Error executing statement: " + e.getMessage());
			}
		}else {
			System.out.println("\n\tDeletion canceled by user.");
		}
	    long end = System.currentTimeMillis();
	    long elapsedTime = end-start;
	    //Prints execution time
    	System.out.printf("\n\tExecution time: (%.3f sec)\n", elapsedTime/1000.0);
	}

}
