/**
 * This class creates Applicant objects with a range of Applicant details.
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class Applicant {

    private String lastName;
    private String firstName;
    private String careerSummary;
    private int age;
    private String gender;
    private String highestDegree;
    private int[] coreSubjectsMarks = new int[4];
    private int salaryExpectations;
    private String availability;

    /**
     * This constructor builds an applicant profile.
     * @param lastName this is the last name of the applicant.
     * @param firstName this is the first name of the applicant.
     * @param careerSummary a description of the applicant's career.
     * @param age an integer of the applicants age that must be between 18 and 100.
     * @param gender a string of the applicants gender. Must be male, female or other.
     * @param highestDegree a string of the highest degree applicant achieved. Must be PHD, Master or Bachelor.
     * @param coreSubjectsMarks a list of 4 integers scores for what the applicant achieved in their subjects.
     * @param salaryExpectations an integer of the applicants salary expectations. Must be greater than 0.
     * @param availability a string of the applicant's avaliability date. Must be in the form dd/mm/yy.
     */
    public Applicant(String lastName, String firstName, String careerSummary, int age, String gender, String highestDegree, int[] coreSubjectsMarks, int salaryExpectations, String availability) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.careerSummary = careerSummary;
        this.age = age;
        this.gender = gender;
        this.highestDegree = highestDegree;
        this.coreSubjectsMarks = coreSubjectsMarks;
        this.salaryExpectations = salaryExpectations;
        this.availability = availability;
    }

    /**
     * This method prints a range of an applicant object's details.
     * @return a string of the applicants details.
     */
    public String printApplicant(){
        String salaryExpectationsString;

        if (salaryExpectations == -1){
            salaryExpectationsString = "n/a";
        } else {
            salaryExpectationsString = Integer.toString(salaryExpectations);
        }

        return lastName + ", " + firstName  + " (" + highestDegree + "): " + careerSummary + ". " + salaryExpectationsString + ". Available: " +  availability;
    }

    /**
     * This method gets the applicant's salary expectations.
     * @return an integer of the applicant's salary expectations.
     */
    public int getSalaryExpectations(){
        return salaryExpectations;
    }

    /**
     * This method gets the applicant's highest degree achieved.
     * @return a String of the applicant's highest degree acheievd.
     */
    public String getHighestDegree(){
        return highestDegree;
    }

    /**
     * This method gets the applicant's availability date.
     * @return a String of the applicant's availability date.
     */
    public String getAvailability(){
        return availability;
    }

    /**
     * This method gets the applicant's average score (WAM) across all 4 of their subjects to be used in a matching
     * applicants to jobs. If an applicant fails to provide a score, a default score of 68 is allocated to them,
     * and average is adjusted accordingly. 
     * @return a double of the applicant's WAM.
     */
    public double getAverageScoreForMatch(){

        double sum = 0;

        //if the score is left empty for the applicant we assign them an average/default score (about a c+)
        int defaultScore = 68;

        for (int score: coreSubjectsMarks){
            if (score == -1){
                score = defaultScore;
            }

            sum += score;
        }

        return sum/4;

    }

    /**
     * This method gets the applicant's average score (WAM) across all 4 of their subjects. If an applicant fails to provide a score, this won't be included in the
     * the average calculation. If all applicant subject scores are empty, an WAM of -1 is allocated.
     * @return a double of the applicant's WAM. If no scores provided, -1 is returned.
     */
    public double getAverageScore(){

        double sum = 0;
        int count = 0;

        for (int score: coreSubjectsMarks){

            //if score =-1 (they didn't provide this option) we don't include this in the average and reduce the denominator in the average calculation.
            if (score == -1){
                count += 1;
            } else {
                sum += score;
            }   
        }
        //to avoid division by zero error
        if (count == 4){
            return -1;
        } else {
           return sum/(4-count); 
        }

        

    }

    /**
     * This method gets the applicant's age.
     * @return a int of the applicant's age.
     */
    public int getAge(){
        return age;
    }

    /**
     * This method gets the applicant's gender.
     * @return a String of the applicant's gender.
     */
    public String getGender(){
        return gender;
    }
    
    /**
     * This method gets the applicant's last name.
     * @return a String of the applicant's last name.
     */
    public String getLastName(){
        return lastName;
    }


}
