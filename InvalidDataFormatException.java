/**
 * This class defines a custom exception class used to throw an exception that occurs when parsing a file and
 * there is an invalid number of data fields per row - 6 for a job's file, and 13 for an applicant's file.
 *
 * COMP90041, Sem2, 2022: Final Project
 * @author: Thomas Carey (831811)
 */
public class InvalidDataFormatException extends Exception{
	
    /** 
	 * This constructor is invoked when no arguement is passed in to create a InvalidDataFormatException object and to print the 
	 * default message of "WARNING: invalid data format in file"
	 */
    public InvalidDataFormatException() {
		super("WARNING: invalid data format in file");
	}

	/**
	 * When an arguement is passed into InvalidDataFormatException constructor this is invoked to enable a custom message to be 
	 * printed when this exception is thrown.
     * @param message A String message to print when the exception is thrown.
	 */ 
    public InvalidDataFormatException(String message) {
		super(message);
	}

}
