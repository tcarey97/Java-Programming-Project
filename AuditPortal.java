import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Collections;

/**
 * This class is designed to take the user into 'The Audit Portal', and display a comprehensive statistic that audits
 * the match making algorithm. 
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class AuditPortal extends ParentPortal {

    private ArrayList<Applicant> finalList;

    /**
     * This constructor enables users to enter the audit portal with a list of jobs and a list of applicants.
     * @param jobs An arrayList of Job objects.
     * @param applicants An arrayList of Applicant objects.
     */
    public AuditPortal(ArrayList<Job> jobs, ArrayList<Applicant> applicants) {
        super(jobs, applicants);
    }

    /**
     * This method encompasses all helper methods within this class to output a comprehensive statistic to audit
     * the match making algorithm.
     */
    public void auditPortalOutput(){

        displayAuditPortalMainMenu();

        if (jobs.size() > 0 && applicants.size() > 0){
            oneMatchedApplicantPerJob();

            //no matches for the audit
            if (finalList.size() == 0){
                System.out.println("Number of successful matches: " + finalList.size());
                return;

            }

            displayfirstThreeLinesOfStatistic();

            displayGenderAndDegreeTypes();

        }
    }

    private void displayAuditPortalMainMenu(){

        String mainMenuText;

        if (jobs.size() == 0){

            mainMenuText = 
                "======================================" + "\n" + 
                "# Matchmaking Audit" + "\n" + 
                "======================================" + "\n" +
                "Available jobs: " + jobs.size() + "\n" +
                "No jobs available for interrogation.";

        } else if (applicants.size() == 0){
            mainMenuText = 
                "======================================" + "\n" + 
                "# Matchmaking Audit" + "\n" + 
                "======================================" + "\n" +
                "Available jobs: " + jobs.size() + "\n" +
                "No applicants available for interrogation.";

        } else {

            mainMenuText = 
                "======================================" + "\n" + 
                "# Matchmaking Audit" + "\n" + 
                "======================================" + "\n" +
                "Available jobs: " + jobs.size() + "\n" +
                "Total number of applicants: " + applicants.size();

        }

        System.out.println(mainMenuText);


    }

    //if there are duplicate applicants in jobs, this method selects the first occurence in the jobs list, and removes the rest.
    private void oneMatchedApplicantPerJob(){

        Matchmaker matchmaker = new Matchmaker(jobs, applicants);
        ArrayList<ArrayList<Applicant> > matchedApplicants = matchmaker.auditMatchMatchmaking();

        finalList = new ArrayList<Applicant>();

        for (int i=0; i<matchedApplicants.size(); i++){
    
            for (int z = 0; z<matchedApplicants.get(i).size(); z++){

                if (!finalList.contains(matchedApplicants.get(i).get(z))){
                    finalList.add(matchedApplicants.get(i).get(z));
                }
                
             }
        }
     

    }

    private void displayfirstThreeLinesOfStatistic(){

        int numberOfSuccessfulMatches = finalList.size();

        String matchedAverageScore = computeAverageScore(finalList);
        String averageScoreAllApplicants = computeAverageScore(applicants);

        String matchedAverageAge = computeAverageAge(finalList);
        String averageAgeAllApplicants = computeAverageAge(applicants);

        String displayString = 
            "Number of successful matches: " + numberOfSuccessfulMatches + "\n" + 
            "Average age: " + matchedAverageAge + " (average age of all applicants: " + averageAgeAllApplicants + ")" + "\n" + 
            "Average WAM: " + matchedAverageScore + " (average WAM of all applicants: " + averageScoreAllApplicants + ")";

        System.out.println(displayString);
        
    }

    private String computeAverageScore(ArrayList<Applicant> listOfApplicants){

        double sumOfAverageScores = 0;
        int count = 0;
        
        DecimalFormat round = new DecimalFormat("0.00");
        String averageScore;

        for (Applicant applicant: listOfApplicants){

            if (applicant.getAverageScore() == -1){
                count += 1;
            } else {
                sumOfAverageScores += applicant.getAverageScore();
            }
            
        }

        if (sumOfAverageScores == 0){
            averageScore = "n/a";

        } else {
            double averageOfAverageScores = sumOfAverageScores/(listOfApplicants.size()-count);

            averageScore = round.format(averageOfAverageScores);
        }

        return averageScore;

    }

    private String computeAverageAge(ArrayList<Applicant> listOfApplicants){

        double sumOfAges = 0;
   
        DecimalFormat round = new DecimalFormat("0.00");
         
        for (Applicant applicant: listOfApplicants){

            sumOfAges += applicant.getAge();
            
        }

        double averageOfAges = sumOfAges/listOfApplicants.size();

        return round.format(averageOfAges);

    }

    private void displayGenderAndDegreeTypes(){

        ArrayList<Double> listOfDistributions = new ArrayList<Double>();
        double distributionOfMales = -1;
        double distributionOfFemales = -1;
        double distributionOfOthers = -1;
        double distributionOfPHD = -1;
        double distributionOfMaster = -1;
        double distributionOfBachelor = -1;

        double numbOfMatchedMales = computeNumberOfMales(finalList);
        double numbOfTotalMales = computeNumberOfMales(applicants);

        if (numbOfTotalMales != 0){
            distributionOfMales = numbOfMatchedMales/numbOfTotalMales;
            listOfDistributions.add(distributionOfMales);
        }
        
        double numbOfMatchedFemales = computeNumberOfFemales(finalList);
        double numbOfTotalFemales = computeNumberOfFemales(applicants);

        if (numbOfTotalFemales != 0){
            distributionOfFemales = numbOfMatchedFemales/numbOfTotalFemales;
            listOfDistributions.add(distributionOfFemales);
        }

        double numbOfMatchedOthers = computeNumberOfOthers(finalList);
        double numbOfTotalOthers = computeNumberOfOthers(applicants);

        if (numbOfTotalOthers != 0){
            distributionOfOthers = numbOfMatchedOthers/numbOfTotalOthers;
            listOfDistributions.add(distributionOfOthers);
        }
        

        double numbOfMatchedPHD = computeNumberOfPHD(finalList);
        double numbOfTotalPHD = computeNumberOfPHD(applicants);

        if (numbOfTotalPHD != 0){
            distributionOfPHD = numbOfMatchedPHD/numbOfTotalPHD;
            listOfDistributions.add(distributionOfPHD);
        }
        

        double numbOfMatchedMaster = computeNumberOfMaster(finalList);
        double numbOfTotalMaster = computeNumberOfMaster(applicants);

        if (numbOfTotalMaster != 0){
            distributionOfMaster = numbOfMatchedMaster/numbOfTotalMaster;
            listOfDistributions.add(distributionOfMaster);
        }
        

        double numbOfMatchedBachelor = computeNumberOfBachelor(finalList);
        double numbOfTotalBachelor = computeNumberOfBachelor(applicants);

        if (numbOfTotalBachelor != 0){
            distributionOfBachelor = numbOfMatchedBachelor/numbOfTotalBachelor;
            listOfDistributions.add(distributionOfBachelor);
        }

        Collections.sort(listOfDistributions, Collections.reverseOrder()); 
        ArrayList<Double> uniqueListOfDistributions = removeDuplicates(listOfDistributions); 

        DecimalFormat round = new DecimalFormat("0.00");
        for (double distributionScore: uniqueListOfDistributions){
            if (distributionOfBachelor == distributionScore){
                System.out.println("Bachelor: " + round.format(distributionOfBachelor));
            }

            if (distributionOfFemales == distributionScore){
                System.out.println("female: " + round.format(distributionOfFemales));
            }

            if (distributionOfMales == distributionScore){
                System.out.println("male: " + round.format(distributionOfMales));
            }

            if (distributionOfMaster == distributionScore){
                System.out.println("Master: " + round.format(distributionOfMaster));
            }

            if (distributionOfOthers == distributionScore){
                System.out.println("other: " + round.format(distributionOfOthers));
            }

            if (distributionOfPHD == distributionScore){
                System.out.println("PHD: " + round.format(distributionOfPHD));
            }
        }

    }

    private double computeNumberOfMales(ArrayList<Applicant> listOfApplicants){

        double numbOfMales = 0;

        for (Applicant applicant: listOfApplicants){
            
            if (applicant.getGender().equals("male")){
                numbOfMales += 1;
            }
        }

        return numbOfMales;

    }

    private double computeNumberOfFemales(ArrayList<Applicant> listOfApplicants){

        double numbOfFemales = 0;

        for (Applicant applicant: listOfApplicants){
            
            if (applicant.getGender().equals("female")){
                numbOfFemales += 1;
            }
        }

        return numbOfFemales;

    }

    private double computeNumberOfOthers(ArrayList<Applicant> listOfApplicants){

        double numbOfOthers = 0;

        for (Applicant applicant: listOfApplicants){
            
            if (applicant.getGender().equals("other")){
                numbOfOthers += 1;
            }
        }

        return numbOfOthers;

    }

    private double computeNumberOfPHD(ArrayList<Applicant> listOfApplicants){

        double numbOfPHD = 0;

        for (Applicant applicant: listOfApplicants){
            
            if (applicant.getHighestDegree().equals("PHD")){
                numbOfPHD += 1;
            }
        }

        return numbOfPHD;

    }

    private double computeNumberOfMaster(ArrayList<Applicant> listOfApplicants){

        double numbOfMaster = 0;

        for (Applicant applicant: listOfApplicants){
            
            if (applicant.getHighestDegree().equals("Master")){
                numbOfMaster += 1;
            }
        }

        return numbOfMaster;

    }

    private double computeNumberOfBachelor(ArrayList<Applicant> listOfApplicants){

        double numbOfBachelor = 0;

        for (Applicant applicant: listOfApplicants){
            
            if (applicant.getHighestDegree().equals("Bachelor")){
                numbOfBachelor += 1;
            }
        }

        return numbOfBachelor;

    }

    private ArrayList<Double> removeDuplicates(ArrayList<Double> listOfDistributions){

        ArrayList<Double> finalListOfDistributions = new ArrayList<Double>();
        
        for (double value: listOfDistributions){
            if (!finalListOfDistributions.contains(value)){
                finalListOfDistributions.add(value);
            }
        }

        return finalListOfDistributions;


    }






}
