/**
 * This class creates Job objects with a range of Job details.
 * COMP90041, Sem2, 2022: Final Project
 * 
 * @author: Thomas Carey (831811)
 */
public class Job {

    private String title;
    private String description;
    private String degree;
    private int salary;
    private String startDate;

    /**
     * This constructor builds an job profile.
     * @param title this is the position title of the job.
     * @param description a description summary of the job.
     * @param degree a degree required for the job.
     * @param salary the salary offered for the job.
     * @param startDate the starting date of the job.
     */
    public Job(String title, String description, String degree, int salary, String startDate) {
        this.title = title;
        this.description = description;
        this.degree = degree;
        this.salary = salary;
        this.startDate = startDate;
    }

    /**
     * This method prints a range of a job object's details.
     * @return a string of the job's details.
     */
    public String listJob(){
        String salaryString;

        if (salary == -1){
            salaryString = "n/a";
        } else {
            salaryString = Integer.toString(salary);
        }

        return title + " (" + description + "). " + degree + ". Salary: " + salaryString + ". Start Date: " + startDate + ".";
    }

    /**
     * This method gets the job's minimum degree requirement.
     * @return a string of the job's minimum degree requirement.
     */
    public String getDegree(){
        return degree;
    }

    /**
     * This method gets the job's starting date.
     * @return a string of the job's starting date.
     */
    public String getStartDate(){
        return startDate;
    }

    /**
     * This method gets the job's offered salary.
     * @return an integer of the job's offered salary.
     */
    public int getSalary(){
        return salary;
    }




}
