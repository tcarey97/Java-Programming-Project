import java.util.ArrayList;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.io.File;

/**
 * This class is designed to take the user into 'The Application Portal', and enable the user to interact given user input. 
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class ApplicationPortal extends ParentPortal{

    private String applicationsFileName;
    private boolean profileCreated = false;
    private int numbOfApplicationsSubmited = 0;

    /**
     * This constructor builds an instance of the applicant portal with a list of applicants and a list of jobs.
     * @param jobs An arrayList of Job objects.
     * @param applicants An arrayList of Applicant objects.
     * @param applicationsFileName A filename that the user wants to write a new application to.
     */
    public ApplicationPortal(ArrayList<Job> jobs, ArrayList<Applicant> applicants, String applicationsFileName){
        super(jobs, applicants);
        this.applicationsFileName = applicationsFileName;

    }

    /**
     * This method prints the welcome display for a user first entering the applicant's portal.
     */
    public void welcomeApplicationPortalMenu(){
        displayWelcomeMessage("welcome_applicant.ascii");
        displayJobsAndApplications();
        displayApplicationPortalCommands();
        
        commands();

    }

    /**
     * This method prints the main menu display for a user that performed at least one action in the applicant's portal.
     */
    public void applicationPortalMenu(){
        displayJobsAndApplications();
        displayApplicationPortalCommands();
        
        commands();

    }

    private void displayJobsAndApplications(){

        int jobsAvailable = jobs.size();

        System.out.println(jobsAvailable + " jobs available. " + numbOfApplicationsSubmited + " applications submitted.");

    
    }

    private void displayApplicationPortalCommands(){

        String displayCommands = "";

        if (profileCreated == false){

            displayCommands = 
                "Please enter one of the following commands to continue:" + "\n" +
                "- create new application: [create] or [c]" + "\n" +
                "- list available jobs: [jobs] or [j]" + "\n" +
                "- quit the program: [quit] or [q]";   

        } else {

            displayCommands = 
                "Please enter one of the following commands to continue:" + "\n" +
                "- list available jobs: [jobs] or [j]" + "\n" +
                "- quit the program: [quit] or [q]";

        }

        System.out.println(displayCommands);
        
    }

    private void commands(){

		System.out.print("> ");
		String command = keyboard.nextLine();

		if ((command.equals("create") || command.equals("c")) && profileCreated == false){
			createNewApplication();

		} else if (command.equals("jobs") || command.equals("j")){
			listAvailableJobs();

		} else if (command.equals("quit") || command.equals("q")){
            System.out.println();

		} else {
            System.out.println("Invalid input! Please enter a valid command to continue: ");
            commands();
        }
        

	}

    private void createNewApplication(){

        PrintWriter outStream = null;
        try {

            outStream = new PrintWriter (new FileOutputStream (applicationsFileName, true));
            
        } catch (FileNotFoundException e) {
            
            System.out.println("File not found.");
        }

        System.out.println("# Create new Application");
        
        //check lastname input
        System.out.print("Lastname: ");
        String lastname = keyboard.nextLine();

        while (lastname.equals("")){
            System.out.print("Ooops! Lastname must be provided: ");
            lastname = keyboard.nextLine();
        }

        //check firstname input
        System.out.print("Firstname: ");
        String firstname = keyboard.nextLine();
        
        while (firstname.equals("")){
            System.out.print("Ooops! Firstname must be provided: ");
            firstname = keyboard.nextLine();
        }

        //careery summary input
        System.out.print("Career Summary: ");
        String careerSummary = keyboard.nextLine();

        //check age input
        System.out.print("Age: ");

        String ageString = keyboard.nextLine();
    
        int age = 0;
        try{
            age = Integer.parseInt(ageString);

        } catch (NumberFormatException e){}
        
        while((age<= 18 || age>= 100)){
            System.out.print("Ooops! A valid age between 18 and 100 must be provided: "); 
            ageString = keyboard.nextLine();

            try{
                age = Integer.parseInt(ageString);

            } catch (NumberFormatException e){}

        }

        //check gender input
        System.out.print("Gender: ");
        String gender = keyboard.nextLine();

        while (!gender.equals("female") && !gender.equals("male") && !gender.equals("other") && !gender.equals("")){
            System.out.print("Invalid input! Please specify Gender: ");
            gender = keyboard.nextLine();
        }

        //check highest degree input
        System.out.print("Highest Degree: ");
        String highestDegree = keyboard.nextLine();

        while (!highestDegree.equals("Bachelor") && !highestDegree.equals("Master") && !highestDegree.equals("PHD") && !highestDegree.equals("")){
            System.out.print("Invalid input! Please specify Highest Degree: ");
            highestDegree = keyboard.nextLine();
        }

        //check coursework input (x4)
        System.out.println("Coursework: ");
        
        System.out.print("- COMP90041: ");
        String COMP90041 = testForValidScore();
        
        System.out.print("- COMP90038: ");
        String COMP90038 = testForValidScore();

        System.out.print("- COMP90007: ");
        String COMP90007 = testForValidScore();

        System.out.print("- INFO90002: ");
        String INFO90002 = testForValidScore();

        //check salary input
        System.out.print("Salary Expectations ($ per annum): ");
        String salaryExpectations = testForValidSalary();

        //check availability input
        System.out.print("Availability: ");
        
        LocalDate today = LocalDate.now();
        LocalDate date = LocalDate.of(0000,01,01);

        String availability = null;

        while (date.isBefore(today)) {
            availability = keyboard.nextLine();

            if (availability.equals("")){
                break;
            }

            String[] availabilityArray = availability.split("/");

            try{

                String day = availabilityArray[0];
                String month = availabilityArray[1];
                String year = availabilityArray[2];

                date = LocalDate.parse("20" + year + "-" + month + "-" + day);

            } catch (Exception e){
                System.out.print("Invalid input! Please specify Availability: ");
                continue;

            }
            

            if (date.isBefore(today)){
                System.out.print("Invalid input! Please specify Availability: ");

            }
        }

        //create timestamp
        Instant instant = Instant.now();
        String timeStamp = Long.toString(instant.getEpochSecond());
        
        //write to applications file
        outStream.println(timeStamp + "," + lastname + "," + firstname + "," + careerSummary + "," + ageString + "," + gender + "," + highestDegree + "," + COMP90041 + "," + COMP90038 + "," + COMP90007 + "," + INFO90002 + "," + salaryExpectations + "," + availability);
        outStream.close();

        //to create a new view for the main menu and restrict commands from using create.
        profileCreated = true;

        applicationPortalMenu();

    }

    private String testForValidScore(){

        String scoreString = keyboard.nextLine();
        
        while (!scoreString.equals("")){

            int score = Integer.parseInt(scoreString);
            
            if (score < 49 || score >100){
                System.out.print("Invalid input! Please specify Coursework: ");
                scoreString = keyboard.nextLine();
                continue;
            }

            return scoreString;

        }

        return scoreString;    

    }

    private void listAvailableJobs(){

        if (jobs.size()>0) { 

            for (int i = 0; i<jobs.size(); i++) {
                System.out.println("[" + (i+1) + "] " + jobs.get(i).listJob());
            }

            if (profileCreated == false){

                applicationPortalMenu();   

            } else {

                System.out.print("Please enter the jobs you would like to apply for (multiple options are possible): ");
                String jobSelectionsString = keyboard.nextLine();

                if (jobSelectionsString.equals("")){
                    
                    applicationPortalMenu();

                } else {
                    jobSelection(jobSelectionsString);
                }

            }

        } else {
            System.out.println("No jobs available.");

            applicationPortalMenu();
        }

    }

    private void jobSelection(String jobSelectionsString){

        ArrayList<Integer> temporaryList = new ArrayList<Integer>();

        while (temporaryList.size() == 0) {

            if (jobSelectionsString.equals("")){
                applicationPortalMenu();
                return;
            }

            String[] jobSelectionsArray = jobSelectionsString.split(",");

            for (String selection: jobSelectionsArray){
                try {
                    
                    int selectionInt = Integer.parseInt(selection);
                    temporaryList.add(selectionInt);

                } catch (NumberFormatException e) {
                    temporaryList.clear();
                    System.out.println("Invalid input! Please enter a valid number to continue: ");
                    jobSelectionsString = keyboard.nextLine();

                }
            }

        }

        int count = 0;
        for (int selection: temporaryList) {
            jobs.remove(selection-1-count);
            count += 1;
            numbOfApplicationsSubmited += 1;
        }

        //write to a permanent storage named "jobSelections.csv"
        PrintWriter outStream = null;
        try {

            outStream = new PrintWriter (new FileOutputStream ("jobSelections.csv", true));

        } catch (FileNotFoundException e) {
            
            File newFile = new File("jobSelections.csv");
        }
        
        //always the first entry is the applicants line number in the applications file.
        outStream.println((applicants.size()) + "," + jobSelectionsString);
        outStream.close();

        applicationPortalMenu();

    }



}

