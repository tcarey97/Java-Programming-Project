import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This class is the parent class of the Application Portal, Job Portal and Audit portal given the commonalities in those classes.
 * COMP90041, Sem2, 2022: Final Project
 *
 * @author: Thomas Carey (831811)
 */
public abstract class ParentPortal {

    /**
     * This public ArrayList of Job objects enables children classes to acesss the list of Jobs parsed from the Job's file
     * and edit the ArrayList depending on the action in the specific portal. 
     */
    public ArrayList<Job> jobs;

    /**
     * This public ArrayList of Applicant objects enables children classes to acesss the list of Applicants parsed from the Applicants's file
     * and edit the ArrayList depending on the action in the specific portal. 
     */
    public ArrayList<Applicant> applicants;

    /**
     * This public scanner object enables users in the applicant and job portal to input information from their keyboad. 
     */
    public Scanner keyboard = new Scanner(System.in);


    /**
     * This default constuctor enables the children objects of the classes ApplicationPortal, HRStaffPortal and AuditPortal to access 
     * the public methods and attributes within this class.
     * @param jobs An arrayList of Job objects.
     * @param applicants An arrayList of Applicant objects.
     */
    public ParentPortal(ArrayList<Job> jobs, ArrayList<Applicant> applicants){
        this.jobs = jobs;
        this.applicants = applicants;
    }
    
    
    
    /**
     * Depending on what portal the user is in - applicant or job - this method will print a welcome message.
     * @param filename A String of a filename containing lines of data in the image of a welcome message.
     */
    public void displayWelcomeMessage(String filename) {

        Scanner inputStream = null;
 
        try{
            inputStream = new Scanner(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Welcome File not found.");
        }

        while(inputStream.hasNextLine())
        {
             System.out.println(inputStream.nextLine());
        }

        inputStream.close();
    }


    /**
     * Depending on what portal the user is in, this method enables user input for salary and tests for input being greater than zero. 
     * Otherwise, it will force the user to re-enter a valid salary. 
     * @return filename A String of either an applicant's salary expectation or a job's salary.
     */
    public String testForValidSalary(){

        String salaryString = keyboard.nextLine();
        
        while (!salaryString.equals("")){

            int salary = Integer.parseInt(salaryString);
            
            if (salary <= 0){
                if (this instanceof ApplicationPortal){
                    System.out.print("Invalid input! Please specify Salary Expectations ($ per annum): ");
                } else {
                    System.out.print("Invalid input! Please specify Salary ($ per annum): ");
                }
                
                salaryString = keyboard.nextLine();
                continue;

            }

            return salaryString; 

        }

        return salaryString;    

    }



}
