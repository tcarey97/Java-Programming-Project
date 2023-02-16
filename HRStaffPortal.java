import java.util.ArrayList;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;
import java.io.FileInputStream;
import java.util.Collections;
import java.io.File;

/**
 * This class is designed to take the user into 'The HRStaff Portal', and enable the user to interact given user input. 
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class HRStaffPortal extends ParentPortal {

    private String jobsFileName;

    /**
     * This constructor builds an instance of the job portal with a list of applicants and a list of jobs.
     * @param jobs An arrayList of Job objects.
     * @param applicants An arrayList of Applicant objects.
     * @param jobsFileName A filename that the user wants to write new jobs to.
     */
    public HRStaffPortal(ArrayList<Job> jobs, ArrayList<Applicant> applicants, String jobsFileName){
        super(jobs, applicants);
        this.jobsFileName = jobsFileName;

    }

    /**
     * This method prints the welcome display for a user first entering the job's portal.
     */
    public void welcomehrStaffPortalMenu(){
        displayWelcomeMessage("welcome_hr.ascii");

        int applicationsReceived = applicants.size();
        System.out.println(applicationsReceived + " applications received.");

        displayHRStaffPortalCommands();
        
        commands();

    }

    /**
     * This method prints the main menu display for a user that performed at least one action in the job's portal.
     */
    public void hrStaffPortalMenu(){

        int applicationsReceived = applicants.size();
        System.out.println(applicationsReceived + " applications received.");

        displayHRStaffPortalCommands();
        commands();

    }

    private void displayHRStaffPortalCommands(){

        String displayCommands = 
            "Please enter one of the following commands to continue:" + "\n" +
            "- create new job: [create] or [c]" + "\n" +
            "- list available jobs: [jobs] or [j]" + "\n" +
            "- list applicants: [applicants] or [a]" + "\n" + 
            "- filter applications: [filter] or [f]" + "\n" +
            "- matchmaking: [match] or [m]" + "\n" +
            "- quit the program: [quit] or [q]";

        System.out.println(displayCommands);
        
    }

    private void commands(){

		System.out.print("> ");
		String command = keyboard.nextLine();

		if (command.equals("create") || command.equals("c")){
			createNewJob();
            hrStaffPortalMenu();

		} else if (command.equals("jobs") || command.equals("j")){
			listAvailableJobs();
            hrStaffPortalMenu();

		} else if (command.equals("applicants") || command.equals("a")){
            sortByAvailability();
            
        } else if (command.equals("filter") || command.equals("f")){
            filterApplicants();

        } else if (command.equals("match") || command.equals("m")){
            Matchmaker matchmaker = new Matchmaker(jobs, applicants);
            matchmaker.displayMatchedApplicantsToJobs();

            hrStaffPortalMenu();

        } else if (command.equals("quit") || command.equals("q")){
            System.out.println();
			
		} else {
            System.out.println("Invalid input! Please enter a valid command to continue: ");
            commands();
        }
        

	}

    private void createNewJob(){

        PrintWriter outStream = null;
        try {

            outStream = new PrintWriter (new FileOutputStream (jobsFileName, true));
            
        } catch (FileNotFoundException e) {
            
            System.out.println("File not found.");
        }


        System.out.println("# Create new Job");
        
        //check position title input
        System.out.print("Position Title: ");
        String positionTitle = keyboard.nextLine();

        while (positionTitle.equals("")){
            System.out.print("Ooops! Position Title must be provided: ");
            positionTitle = keyboard.nextLine();
        }

        //check position Description input
        System.out.print("Position Description: ");
        String positionDescription = keyboard.nextLine();

        //check minimum degree input
        System.out.print("Minimum Degree Requirement: ");
        String minimumDegree = keyboard.nextLine();

        while (!minimumDegree.equals("Bachelor") && !minimumDegree.equals("Master") && !minimumDegree.equals("PHD") && !minimumDegree.equals("")){
            System.out.print("Invalid input! Please specify Minimum Degree Requirement: ");
            minimumDegree = keyboard.nextLine();
        }

        //check salary input
        System.out.print("Salary ($ per annum): ");
        String salaryString = testForValidSalary();

        //check start date input
        System.out.print("Start Date: ");
        String startDate = testForValidStartDate();

        //create timestamp
        Instant instant = Instant.now();
        String timeStamp = Long.toString(instant.getEpochSecond());
        
        //write to applications file
        outStream.println(timeStamp + "," + positionTitle + "," + positionDescription + "," + minimumDegree + "," + salaryString + "," + startDate);
        outStream.close();

        int salaryInt;

        if (salaryString.equals("")){
            salaryInt = -1;

        } else {
            salaryInt = Integer.parseInt(salaryString);
        }
        
        Job newJob = new Job(positionTitle, positionDescription, minimumDegree, salaryInt, startDate);
        jobs.add(newJob);
        

    }

    private String testForValidStartDate(){

        String startDate = keyboard.nextLine();

        LocalDate date = null;

        while (date == null){

            if (startDate.equals("")){
                System.out.print("Ooops! Start Date must be provided: ");
                startDate = keyboard.nextLine();
                continue;
            }

            String[] startDateArray = startDate.split("/");

            try {

                String day = startDateArray[0];
                String month = startDateArray[1];
                String year = startDateArray[2];

                date = LocalDate.parse("20" + year + "-" + month + "-" + day);

            } catch (Exception e){
                System.out.print("Invalid input! Please specify Start Date: ");
                startDate = keyboard.nextLine();
            }

        }

        return startDate;

    }


    private void listAvailableJobs(){

        if (jobs.size() == 0){
            System.out.println("No jobs available.");
            return;
        }

        //two dimensional arraylist
        ArrayList<ArrayList<String> > avaliableJobsWithApplicants = new ArrayList<ArrayList<String> >();

        for (Job job: jobs){
            avaliableJobsWithApplicants.add(new ArrayList<String>());
        }

        Scanner inputStream = null;

        try{
            inputStream = new Scanner(new FileInputStream("jobSelections.csv"));
            
        } catch (FileNotFoundException e){
            for (int i=0; i<avaliableJobsWithApplicants.size(); i++){
                System.out.println("[" + (i+1) + "] " + jobs.get(i).listJob());
            } 
            
            return; 
        }

        
        while(inputStream.hasNextLine()){

            String applicantJobs = inputStream.nextLine();
            String[] applicantJobsArray = applicantJobs.split(",");

            int applicantNumber = Integer.parseInt(applicantJobsArray[0]);

            for (int i=1; i<applicantJobsArray.length; i++){

                int jobSelected = Integer.parseInt(applicantJobsArray[i]) - 1;

                avaliableJobsWithApplicants.get(jobSelected).add(applicants.get(applicantNumber).printApplicant());   

            }   

        }

        for (int i=0; i<avaliableJobsWithApplicants.size(); i++){

            System.out.println("[" + (i+1) + "] " + jobs.get(i).listJob());

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){

                char letter = (char)(z+97);

                System.out.println("    [" + (letter) + "] " + avaliableJobsWithApplicants.get(i).get(z));

            }
        }

    }

    private void listSortedApplicants(ArrayList<Applicant> sortedApplicants){

        if (sortedApplicants.size()>0) { 

            for (int i = 0; i<sortedApplicants.size(); i++) {
                System.out.println("[" + (i+1) + "] " + sortedApplicants.get(i).printApplicant());   
            }

        } else {
            System.out.println("No applicants available.");
        }

        hrStaffPortalMenu();

    }

    

    private void filterApplicants(){
        System.out.print("Filter by: [lastname], [degree] or [wam]: ");

        String userInput = keyboard.nextLine();

        if (applicants.size() == 0){
            System.out.println("No applicants available.");
            hrStaffPortalMenu();
            return;
        }

        if (userInput.equals("lastname")){
            sortByLastName();

        } else if (userInput.equals("degree")){
            sortByDegree();

        } else if (userInput.equals("wam")){
            sortByWAM();

        } else {
            hrStaffPortalMenu();
            return;
        }



    }

    private void sortByLastName(){

        ArrayList<String> listOfLastNames = new ArrayList<String>();
        ArrayList<Applicant> sortedApplicantsByLastName = new ArrayList<Applicant>();

        for (Applicant applicant: applicants){
            listOfLastNames.add(applicant.getLastName());
            
        }

        Collections.sort(listOfLastNames);
        
        //remove duplicate names
        ArrayList<String> finalListOfLastNames = new ArrayList<String>();
        
        for (String lastName: listOfLastNames){
            if (!finalListOfLastNames.contains(lastName)){
                finalListOfLastNames.add(lastName);
            }
        }

        for (String lastName: finalListOfLastNames){
            for (Applicant applicant: applicants){
                
                if (applicant.getLastName().equals(lastName)){
                    sortedApplicantsByLastName.add(applicant);
                }
            }
            
        }

        listSortedApplicants(sortedApplicantsByLastName);  

    }

    private void sortByDegree(){

        String [] listOfDegrees = {"PHD", "Master", "Bachelor", "n/a"};

        ArrayList<Applicant> sortedApplicantsByDegrees = new ArrayList<Applicant>();

        for (String degree: listOfDegrees){
            for (Applicant applicant: applicants){
                
                if (applicant.getHighestDegree().equals(degree)){
                    sortedApplicantsByDegrees.add(applicant);
                }
            }
            
        }

        listSortedApplicants(sortedApplicantsByDegrees);  

    }

    private void sortByWAM(){

        ArrayList<Double> listOfWAMs = new ArrayList<Double>();
        ArrayList<Applicant> sortedApplicantsByWAMs = new ArrayList<Applicant>();

        for (Applicant applicant: applicants){
            listOfWAMs.add(applicant.getAverageScore());
            
        }

        Collections.sort(listOfWAMs, Collections.reverseOrder());
        
        //remove duplicate names
        ArrayList<Double> finalListOfWAMs = new ArrayList<Double>();
        
        for (double wam: listOfWAMs){
            if (!finalListOfWAMs.contains(wam)){
                finalListOfWAMs.add(wam);
            }
        }

        for (double wam: finalListOfWAMs){
            for (Applicant applicant: applicants){
                
                if (applicant.getAverageScore() == wam){
                    sortedApplicantsByWAMs.add(applicant);
                }
            }
            
        }

        listSortedApplicants(sortedApplicantsByWAMs);  

    }

    private void sortByAvailability(){

        ArrayList<String> listOfDates = new ArrayList<String>();
        ArrayList<Applicant> sortedApplicantsByDates = new ArrayList<Applicant>();

        for (Applicant applicant: applicants){
            listOfDates.add(applicant.getAvailability());
            
        }

        Collections.sort(listOfDates);
        
        //remove duplicate names
        ArrayList<String> finalListOfDates = new ArrayList<String>();
        
        for (String date: listOfDates){
            if (!finalListOfDates.contains(date)){
                finalListOfDates.add(date);
            }
        }

        for (String date: finalListOfDates){
            for (Applicant applicant: applicants){
                
                if (applicant.getAvailability().equals(date)){
                    sortedApplicantsByDates.add(applicant);
                }
            }
            
        }

        listSortedApplicants(sortedApplicantsByDates);  

    }




}
