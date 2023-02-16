import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;

/**
 * This class contains the logic for the HR Assistant's matchmaking algorithm.
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class Matchmaker {

    private ArrayList<Job> jobs;
    private ArrayList<Applicant> applicants;
    private ArrayList<ArrayList<Applicant> > avaliableJobsWithApplicants;
    private boolean noApplications = true;

    /**
     * This constructor builds an instance of the Matchmaker with a list of applicants and a list of jobs.
     * @param jobs An arrayList of Job objects.
     * @param applicants An arrayList of Applicant objects.
     */
    public Matchmaker(ArrayList<Job> jobs, ArrayList<Applicant> applicants) {
        this.jobs = jobs;
        this.applicants = applicants;
    }

    /**
     * This method encompasses all helper methods within this class to output a list of all the jobs that were matched
     * to with applicants, based on certain selection/filtering criteria.
     * the match making algorithm.
     */
    public void displayMatchedApplicantsToJobs(){
        
        listApplicantsUnderJobs();

        if (jobs.size() == 0){
            System.out.println("No jobs available.");
            return;
        } else if (noApplications){
            System.out.println("No applicants available.");
            return;
        }

        matchDegree();
        matchDates();
        
        matchSalaryExpectations();
        remainingApplicantsMatched();
        finalMatchedList();

        for (int i=0; i<avaliableJobsWithApplicants.size(); i++){

            System.out.println("[" + (i+1) + "] " + jobs.get(i).listJob());

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){

                System.out.println("    Applicant match: " + avaliableJobsWithApplicants.get(i).get(z).printApplicant());

            }
        } 
      
    
    }

    private void listApplicantsUnderJobs(){

        //two dimensional arraylist
        avaliableJobsWithApplicants = new ArrayList<ArrayList<Applicant> >();

        for (Job job: jobs){
            avaliableJobsWithApplicants.add(new ArrayList<Applicant>());
        }

        Scanner inputStream = null;

        try{
            inputStream = new Scanner(new FileInputStream("jobSelections.csv"));
            
        } catch (FileNotFoundException e){
            return;
        }
    
        while(inputStream.hasNext()){

            String applicantJobs = inputStream.nextLine();

            if (applicantJobs == ""){
                return;
            }

            noApplications = false;
            String[] applicantJobsArray = applicantJobs.split(",");

            int applicantNumber = Integer.parseInt(applicantJobsArray[0]);

            for (int i=1; i<applicantJobsArray.length; i++){

                int jobSelected = Integer.parseInt(applicantJobsArray[i]) - 1;

                avaliableJobsWithApplicants.get(jobSelected).add(applicants.get(applicantNumber));   

            }   

        }
 
    }

    private ArrayList<ArrayList<Applicant> > createCopyOfArray (ArrayList<ArrayList<Applicant> > avaliableJobsWithApplicants){
        
        ArrayList<ArrayList<Applicant> > tempArrayList = new ArrayList<ArrayList<Applicant> >();

        for (int i=0; i<jobs.size(); i++){
            tempArrayList.add(new ArrayList<Applicant>());

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){
                tempArrayList.get(i).add(avaliableJobsWithApplicants.get(i).get(z));
             }
        }

        return tempArrayList;
        
    }

    private void matchDegree(){

        ArrayList<ArrayList<Applicant> > tempArrayList = createCopyOfArray(avaliableJobsWithApplicants);
       
        for (int i=0; i<jobs.size(); i++){

            //we use a count to keep track of the reduced arraylist size when we remove elements from it that do not meet the matching requirements.
            int count = 0;

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){

                Applicant applicantUnderJob = avaliableJobsWithApplicants.get(i).get(z);
                
                //an applicant must have a degree equal to or higher than the one listed in the job - otherwise they are removed as an applicant from thay job.
                if (jobs.get(i).getDegree().equals("Bachelor") && !applicantUnderJob.getHighestDegree().equals("Bachelor") && !applicantUnderJob.getHighestDegree().equals("Master") && !applicantUnderJob.getHighestDegree().equals("PHD")) {
                    
                    tempArrayList.get(i).remove(z-count);
                    count += 1;
    
                } else if (jobs.get(i).getDegree().equals("Master") && !applicantUnderJob.getHighestDegree().equals("Master") && !applicantUnderJob.getHighestDegree().equals("PHD")){
      
                    tempArrayList.get(i).remove(z-count);
                    count += 1;
            
                } else if (jobs.get(i).getDegree().equals("PHD") && !applicantUnderJob.getHighestDegree().equals("PHD")){
                    
                    tempArrayList.get(i).remove(z-count);
                    count += 1;
                    
                } 
                
             }

        }

        avaliableJobsWithApplicants = tempArrayList;
        
    }

    private void matchDates(){

        ArrayList<ArrayList<Applicant> > tempArrayList = createCopyOfArray(avaliableJobsWithApplicants);

        for (int i=0; i<jobs.size(); i++){

            //converting job date into localDate
            String[] startDateArray = jobs.get(i).getStartDate().split("/");
            String day = startDateArray[0];
            String month = startDateArray[1];
            String year = startDateArray[2];
            LocalDate startDateOfJob = LocalDate.parse("20" + year + "-" + month + "-" + day);

            int count = 0;

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){

         
                if (!avaliableJobsWithApplicants.get(i).get(z).getAvailability().equals("n/a")){

                    
                    String[] availabilityArray = avaliableJobsWithApplicants.get(i).get(z).getAvailability().split("/");

                    String availabilityDay = availabilityArray[0];
                    String availabilityMonth = availabilityArray[1];
                    String availabilityYear = availabilityArray[2];

                    LocalDate availabilityForApplicant = LocalDate.parse("20" + availabilityYear + "-" + availabilityMonth + "-" + availabilityDay);

                    if (startDateOfJob.isBefore(availabilityForApplicant)){
                        
                        tempArrayList.get(i).remove(z-count);
                        count += 1;
                    }
                }

            }

        }

        avaliableJobsWithApplicants = tempArrayList;
        
    }

        
    private void matchSalaryExpectations(){
        
        ArrayList<ArrayList<Applicant> > tempArrayList = createCopyOfArray(avaliableJobsWithApplicants);

        for (int i=0; i<jobs.size(); i++){

            int count = 0;

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){

                //applicants are unlikely to want a job that has salary less than 20,000 from their expectations. So this is a lower bound on what the salary can be.
                int salaryLowerBound = avaliableJobsWithApplicants.get(i).get(z).getSalaryExpectations() - 20000;

                //if the actual job's salary is alot higher than their expectations, then the applicant probably isn't well suited to the job given they have too low of expectations for the job;
                int salaryUpperBound = avaliableJobsWithApplicants.get(i).get(z).getSalaryExpectations() + 20000;

                if (jobs.get(i).getSalary() < salaryLowerBound || jobs.get(i).getSalary() > salaryUpperBound){
                    
                    tempArrayList.get(i).remove(z-count);
                    count += 1;
                }

            }

        }

        avaliableJobsWithApplicants = tempArrayList;
           
    }

    //this is to find who has the highest average for all scores. If there is a tie, then it's first in best dressed for who applied earlier as this will show up sooner on the list and win the highest score index.
    private void remainingApplicantsMatched(){

        for (int i=0; i<jobs.size(); i++){

            double max = 0;
            int highestScoreIndex = -1;

            for (int z = 0; z<avaliableJobsWithApplicants.get(i).size(); z++){

                double applicantAverageSubjectsScore = avaliableJobsWithApplicants.get(i).get(z).getAverageScoreForMatch();

                if (applicantAverageSubjectsScore>max){
                    max = applicantAverageSubjectsScore;
                    highestScoreIndex = z;

                }

            }

            if (avaliableJobsWithApplicants.get(i).size()>0){
                Applicant winningApplicantforJob = avaliableJobsWithApplicants.get(i).get(highestScoreIndex);

                avaliableJobsWithApplicants.get(i).clear();

                avaliableJobsWithApplicants.get(i).add(winningApplicantforJob);

            }

        }

    }

    private void finalMatchedList(){

        ArrayList<ArrayList<Applicant> > tempArrayList = createCopyOfArray(avaliableJobsWithApplicants);

        int count = 0;
        for (int i=0; i<jobs.size(); i++){

            if (avaliableJobsWithApplicants.get(i).size() == 0){
                tempArrayList.remove(i-count);

                count += 1;
            }

        }

        avaliableJobsWithApplicants = tempArrayList;

    }

    /**
    * This method is used to get a list of all the applicants who got matched to a job, 
    * assuming that all applicants applied to every job.
    *
    * @return avaliableJobsWithApplicants A two dimensional arraylist of Applicant objects, where dimension 1 represents
    * the job number and dimension 2 represents a list of the applicants who got matched to that job in dimension 1.
    */
    public ArrayList<ArrayList<Applicant> > auditMatchMatchmaking(){

        avaliableJobsWithApplicants = new ArrayList<ArrayList<Applicant> >();

        for (int i=0; i<jobs.size(); i++){
            avaliableJobsWithApplicants.add(new ArrayList<Applicant>());

            for (int z=0; z<applicants.size(); z++){
                avaliableJobsWithApplicants.get(i).add(applicants.get(z));
            }

        }

        matchDegree();
        matchDates();
        matchSalaryExpectations();
        remainingApplicantsMatched();
        finalMatchedList();

        return avaliableJobsWithApplicants;

    }



}
