import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.io.File;

/**
 * This class analyses the user's command line arguements takes the user to the correct portal based on the valid role defined.
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class HRAssistant {

    private ArrayList<Job> jobs;
    private ArrayList<Applicant> applicants;
    private String applicationsFileName;
    private String jobsFileName;
    
    /**
     * This constructor builds an insance of the starting user portal (HRAssistant) with a list of jobs and applicants 
     * defined through inputting their command-line arguements.
     * @param jobs An arrayList of Job objects.
     * @param applicants An arrayList of Applicant objects.
     */
    public HRAssistant(ArrayList<Job> jobs, ArrayList<Applicant> applicants){
        this.jobs = jobs;
        this.applicants = applicants;

    }

    /**
     * The main method. Runs the HRAssistant program, and initialises an empty arraylist of Applicant objects and an 
     * empty arraylist of Job objects. This is then passed into creating an instance of the HRAssistant act on
     * user command-line arguements.
     * @param args The command line arguments.
     **/
    public static void main(String[] args) {

        // Runs the HRAssistant program
      
        ArrayList<Applicant> applicants = new ArrayList<Applicant>();
        ArrayList<Job> jobs = new ArrayList<Job>();

        HRAssistant hrAssistant = new HRAssistant(jobs, applicants);

        hrAssistant.commandLineCheck(args);
  

    }


    private void commandLineCheck(String[] args){

        if (args.length > 0){
            
            boolean rFlagInCommandLine = false;
            int rFlagIndex = -1;
            int jFlagIndex = -1;
            int aFlagIndex = -1;


            boolean jFlagInCommandLine = false;
            boolean aFlagInCommandLine = false;

            if (checkForHelp(args)){
                help();
                return;
            }
            
            for (int i=0; i<args.length; i++){

                if ((args[i].equals("-r")) || args[i].equals("--role")){
                    rFlagInCommandLine = true;
                    rFlagIndex = i;

                } else if ((args[i].equals("-j")) || args[i].equals("--jobs")){
                    jFlagInCommandLine = true;
                    jFlagIndex = i;

                } else if ((args[i].equals("-a")) || args[i].equals("--applications")){
                    aFlagInCommandLine = true; 
                    aFlagIndex = i;

                }          
            }

            if (rFlagInCommandLine) {
                if (userRolesCheck(args, rFlagIndex)){

                    if (jFlagInCommandLine && aFlagInCommandLine) {

                        if (fileCheck(args, jFlagIndex, "jobs") == false || fileCheck(args, aFlagIndex, "applications") == false){
                            help();
                            return;
                        }

                        toPortal(args, rFlagIndex);

                    } else if (jFlagInCommandLine && aFlagInCommandLine == false){
                        
                        if (fileCheck(args, jFlagIndex, "jobs") == false){
                            help();
                            return;
                        } else {
                            defaultFile("applications.csv");
                            applicationsFileName = "applications.csv";
                        }

                        toPortal(args, rFlagIndex);

                    } else if (jFlagInCommandLine == false && aFlagInCommandLine){

                        if (fileCheck(args, aFlagIndex, "applications") == false){
                            help();
                            return;
                        } else {
                            defaultFile("jobs.csv");
                            jobsFileName = "jobs.csv";
                        }

                        toPortal(args, rFlagIndex);

                    } else {

                        defaultFile("applications.csv");
                        applicationsFileName = "applications.csv";

                        defaultFile("jobs.csv");
                        jobsFileName = "jobs.csv";

                        toPortal(args, rFlagIndex);

                    }

                }

            } else {
                help();
                return;
            }          

        } else {
            help();

        }
            
    }

    private boolean checkForHelp(String[] args){
        //checking for help even if there is many other flags in the command line. It takes the highest priority.
        for (int i=0; i<args.length; i++) {
            if (args[i].equals("--help") || args[i].equals("-h")){
                return true;
            }
                
        }

        return false;

    }

    private boolean userRolesCheck(String[] args, int roleFlagIndex){

        //arguement is after the flag
        int roleFlagArguementIndex = roleFlagIndex+1;
        String roleFlagArguement = args[roleFlagArguementIndex];

        //if there is something after the role flag
        if (roleFlagIndex<args.length-1){
                
            if (noFlagArguementCheck(roleFlagArguement)){

                System.out.println("ERROR: no role defined.");
                help();
                return false;

            } else if (!roleFlagArguement.equals("hr") && !roleFlagArguement.equals("applicant") && !roleFlagArguement.equals("audit")){

                System.out.println("ERROR: " + roleFlagArguement + " is not a valid role.");
                help();

                return false;

            } else {
                return true;
            }
            
        
        } 
        //if there is nothing after the role flag
        System.out.println("ERROR: no role defined.");
        help();

        return false;
        

    }


    private void toPortal(String[] args, int roleFlagIndex){

        //arguement is after the flag
        int roleFlagArguementIndex = roleFlagIndex+1;
        String roleFlagArguement = args[roleFlagArguementIndex];
       
        if (roleFlagArguement.equals("hr")){

            HRStaffPortal hrStaffPortal = new HRStaffPortal(jobs, applicants, jobsFileName);

            hrStaffPortal.welcomehrStaffPortalMenu();
                
        } else if (roleFlagArguement.equals("applicant")){

            ApplicationPortal applicationPortal = new ApplicationPortal(jobs, applicants, applicationsFileName);

            applicationPortal.welcomeApplicationPortalMenu();

        } else if (roleFlagArguement.equals("audit")){
               
            AuditPortal auditportal = new AuditPortal(jobs, applicants);
            auditportal.auditPortalOutput();
                
        } 

    
    }

    //if true then it means the flag arguement is not valid
    private boolean noFlagArguementCheck(String flagArguement){

         //the list of inputs immediatelfollowing role flag that determine it to be no role defined
        String [] noRoleDefinedInputs = {"-a", "--applications", "-j", "--jobs", "-r", "--role"};

        for (String inputs: noRoleDefinedInputs){
                if (inputs.equals(flagArguement)){
                    return true;
                    
                }
            }
        
        return false;

    }

    //returns true for a file, false otherwise.
    private boolean fileCheck(String[] args, int flagIndex, String typeOfFile){
  
        //if there is something after the jobs flag
        if (flagIndex<args.length-1){      
            

            int flagArguementIndex = flagIndex+1;
            String flagArguement = args[flagArguementIndex];
                   
            if (noFlagArguementCheck(flagArguement)){

                return false;

            //and that something after the flag is valid
            } else {

                Scanner inputStream = null;

                try{
                    inputStream = new Scanner(new FileInputStream(flagArguement));


                    //filter by either application or jobs flag
                    if (typeOfFile.equals("jobs")){
                        jobsFileName = flagArguement;
                        parsingJobsFile(inputStream);
                    } else {
                        parsingApplicationsFile(inputStream);
                        applicationsFileName = flagArguement;
                    }
                    
                    return true;
                }
                catch (FileNotFoundException e)
                {
                    File newFile = new File(flagArguement);
                    
                    if (typeOfFile.equals("jobs")){
                        jobsFileName = flagArguement;
                    } else {
                        applicationsFileName = flagArguement;
                    }
                    return true;
                }

            }   

        } else {
            return false;
        }

    }

    private void defaultFile(String filename){

        Scanner inputStream = null;

        try{
            inputStream = new Scanner(new FileInputStream(filename));

            if (filename.equals("jobs.csv")){
                parsingJobsFile(inputStream);
            } else { 
                parsingApplicationsFile(inputStream);
            }
            
        }
        catch (FileNotFoundException e){
            if (filename.equals("jobs.csv")){
                File newFile = new File("jobs.csv");
            } else { 
                File newFile = new File("applications.csv");
            }
        }


    }

    

    private void help(){
    
        String helpDisplay = 
            "HRAssistant - COMP90041 - Final Project" + "\n\n" + 

            "Usage: java HRAssistant [arguments]" + "\n\n" +

            "Arguments:" + "\n" + 
            "    -r or --role            Mandatory: determines the user's role" + "\n" +
            "    -a or --applications    Optional: path to applications file" + "\n" +
            "    -j or --jobs            Optional: path to jobs file" + "\n" +
            "    -h or --help            Optional: print Help (this message) and exit";
                    
        System.out.println(helpDisplay);
        
    }
    
    //need to do the three exceptions to parsing the jobs file.
    private void parsingJobsFile(Scanner inputStream){

        //skip first line
        if (inputStream.hasNextLine()){
            inputStream.nextLine(); 
        }

        int lineCount = 1; 

        while (inputStream.hasNextLine()){

            lineCount += 1;

            String jobLine = inputStream.nextLine();

            //properList is after quotes have been checked for in the datafields
            ArrayList<String> properList = checkForQuotes(jobLine);
            Job job;

            try {
                if (properList.size() == 6) {
                    job = analyseJobDataFields(properList, lineCount);

                    if (job != null) {
                        jobs.add(job);
                    }

                } else {
                    throw new InvalidDataFormatException("WARNING: invalid data format in jobs file in line " + lineCount);
                }
            } catch (InvalidDataFormatException e){
                System.out.println(e.getMessage());

            } catch (InvalidCharacteristicException e){
                System.out.println("WARNING: invalid characteristic in jobs file in line " + lineCount);
            }


        }


        inputStream.close();
            

    }


    private void parsingApplicationsFile(Scanner inputStream) {

        //skip first line
        if (inputStream.hasNextLine()){
            inputStream.nextLine();
        }    
        
        int lineCount = 1;

         while (inputStream.hasNextLine()) {

            lineCount += 1;

            String applicantLine = inputStream.nextLine();
       
            ArrayList<String> properList = checkForQuotes(applicantLine);
            Applicant applicant;

            try {
                if (properList.size() == 13) {
                    applicant = analyseApplicantDataFields(properList, lineCount);

                    if (applicant != null) {
                        applicants.add(applicant);

                    }

                } else {
                    throw new InvalidDataFormatException("WARNING: invalid data format in applications file in line " + lineCount);
                }
            } catch (InvalidDataFormatException e){
                System.out.println(e.getMessage());
            } catch (NumberFormatException e){
                System.out.println("WARNING: invalid number format in applications file in line " + lineCount);
            } catch (InvalidCharacteristicException e){
                System.out.println("WARNING: invalid characteristic in applications file in line " + lineCount);
            }

            // System.out.println();

        }

        inputStream.close();


    }

    private ArrayList<String> checkForQuotes(String fileLine) {
        
        //acount for in the applicant file row it ending with a empty datafield.
        if (fileLine.charAt(fileLine.length()-1) == ','){
            fileLine += " ";
        }

        //split the string into array seperated by ","
        String[] fileLineArray = fileLine.split(",");
        int numberofDataFields = fileLineArray.length;

        //testing for quotes in fields of array 
        ArrayList<String> properList = new ArrayList<String>();
        boolean inQuote = false;
        String stringConcat = "";

        for (int i = 0; i< numberofDataFields; i++){
            //if the field in the array contains the left quote mark of the pair of quote marks "" start string concatenation. inQuote == false, helps judge left quote mark.
            if (fileLineArray[i].contains("\"") && inQuote == false){
                inQuote = true;
                stringConcat += fileLineArray[i] + ",";

            //if the field contains the right quote mark then the quote is closed and we can concaatenate all the string text from the left quote mark.   
            } else if (fileLineArray[i].contains("\"") && inQuote == true){
                inQuote = false;
                stringConcat += fileLineArray[i];
                //we have found all the data between the quotes despite them being in different fields and formatted the array correctly now by properList'.
                properList.add(stringConcat);
            //keep concatenating 
            } else if (inQuote == true){
                stringConcat += fileLineArray[i] + ",";
            } else {
                properList.add(fileLineArray[i]);
            }

        }

        return properList;

    }

    private Applicant analyseApplicantDataFields(ArrayList<String> properList, int lineCount) throws InvalidCharacteristicException, NumberFormatException {

        boolean InvalidCharacteristicExceptionInLine = false;
        boolean NumberFormatExceptionInLine = false;


        String lastName = properList.get(1);

        String firstName = properList.get(2);

        if (lastName.equals("") || firstName.equals("")){
            throw new InvalidCharacteristicException();
        }
        
        String careerSummary = properList.get(3);
        
        if (careerSummary.equals("")){
            careerSummary = "n/a";
            InvalidCharacteristicExceptionInLine = true;

        }

        int age = testForNumberFormatException(properList.get(4));    

        //skips this line when parsing the file's lines.
        if (age == -1){
            throw new NumberFormatException();
        } 
        
        if (age <= 18 || age >=100){ 
            throw new InvalidCharacteristicException();
        } 
        
        String gender = properList.get(5);
        gender = testForValidGender(gender, lineCount);

        if (gender.equals("n/a")){
            InvalidCharacteristicExceptionInLine = true;

        }

        String highestDegree = properList.get(6);
        highestDegree = testForValidDegree(highestDegree);

        if (highestDegree.equals("n/a")){
            InvalidCharacteristicExceptionInLine = true;
        }

        int COMP90041 = testForNumberFormatException(properList.get(7));
        int COMP90038 = testForNumberFormatException(properList.get(8));
        int COMP90007 = testForNumberFormatException(properList.get(9));
        int COMP90002 = testForNumberFormatException(properList.get(10));
        int[] coreSubjectsMarks = {COMP90041, COMP90038, COMP90007, COMP90002};

        for (int i = 0; i<coreSubjectsMarks.length; i++){
            if (coreSubjectsMarks[i] == -1){
                NumberFormatExceptionInLine = true;
            }

            coreSubjectsMarks[i] = testForValidScore(coreSubjectsMarks[i]);

            if (coreSubjectsMarks[i] == -1){
                InvalidCharacteristicExceptionInLine = true;
            }
        }

        int salaryExpectations = testForNumberFormatException(properList.get(11));

        if (salaryExpectations == -1){
            NumberFormatExceptionInLine = true;
        }

        salaryExpectations = testForValidSalaryExpectation(salaryExpectations);

        if (salaryExpectations == -1){
            InvalidCharacteristicExceptionInLine = true;
        }

        String availability = properList.get(12);

        try {
            if (availability.split("/").length != 3){
                throw new InvalidCharacteristicException();

            }
        } catch (InvalidCharacteristicException e){
            InvalidCharacteristicExceptionInLine = true;
            availability = "n/a";
        }

        
        if (NumberFormatExceptionInLine == true){
            System.out.println("WARNING: invalid number format in applications file in line " + lineCount);
        }

        if (InvalidCharacteristicExceptionInLine == true){
            System.out.println("WARNING: invalid characteristic in applications file in line " + lineCount);
        }

        Applicant applicant = new Applicant(lastName, firstName, careerSummary, age, gender, highestDegree, coreSubjectsMarks, salaryExpectations, availability);

        return applicant;


    }

    private int testForNumberFormatException(String DataField){  

        try{

            int intDataField = Integer.parseInt(DataField);

            return intDataField;
            
        } catch (NumberFormatException e) {

            return -1;

        }
        
    }

    private String testForValidGender(String gender, int lineCount){

        try{

            if (gender.equals("female") || gender.equals("male") || gender.equals("other")){
                return gender;
                
            } else {
                throw new InvalidCharacteristicException();
            }

        } catch (InvalidCharacteristicException e) {
            // System.out.println("WARNING: invalid characteristic in applications file in line " + lineCount);
            return "n/a";
        } 
    }

    private String testForValidDegree(String degree){

        try{

            if (degree.equals("Bachelor") || degree.equals("Master") || degree.equals("PHD")){
                return degree;
                
            } else {
                throw new InvalidCharacteristicException();
            }

        } catch (InvalidCharacteristicException e) {
            return "n/a";
        } 

    }

    private int testForValidScore(int score){
        try{

            if (score >=49 && score <= 100){
                return score;
                
            } else {
                throw new InvalidCharacteristicException();
            }

        } catch (InvalidCharacteristicException e) {
            return -1;
        } 

    }

    private int testForValidSalaryExpectation(int salary){
        try{

            if (salary > 0){
                return salary;
                
            } else {
                throw new InvalidCharacteristicException();
            }

        } catch (InvalidCharacteristicException e) {
            
            return -1;
        } 

    }



    private Job analyseJobDataFields(ArrayList<String> properList, int lineCount) throws InvalidCharacteristicException, NumberFormatException{
            
        boolean InvalidCharacteristicExceptionInLine = false;
        boolean NumberFormatExceptionInLine = false;

        String title = properList.get(1);

        if (title.equals("")){
            throw new InvalidCharacteristicException();
        }

        String description = properList.get(2);
        
        try{
            if (description.equals("")){
                throw new InvalidCharacteristicException(); 
            }
        } catch (InvalidCharacteristicException e){
            InvalidCharacteristicExceptionInLine = true;
        }
        
        String degree = properList.get(3);
        degree = testForValidDegree(degree);

        if (degree.equals("n/a")){
            InvalidCharacteristicExceptionInLine = true;
        }

        int salary = testForNumberFormatException(properList.get(4));

        if (salary == -1){
            NumberFormatExceptionInLine = true;
        }
        
        salary = testForValidSalaryExpectation(salary);

        if (salary == -1){
            InvalidCharacteristicExceptionInLine = true;
        }  

        String startDate = properList.get(5);

        if (title.equals("") || startDate.split("/").length != 3){
            throw new InvalidCharacteristicException();
        }


        if (NumberFormatExceptionInLine == true){
            System.out.println("WARNING: invalid number format in jobs file in line " + lineCount);
        }

        if (InvalidCharacteristicExceptionInLine == true){
            System.out.println("WARNING: invalid characteristic in jobs file in line " + lineCount);
        }

        Job job = new Job(title, description, degree, salary, startDate);

        return job;


    }



    
}

