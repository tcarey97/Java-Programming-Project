/**
 * This class defines a custom exception class used to throw an exception that occurs when parsing a file and the datafield
 * does not accommodate a specific value given certain criteria for the data field.
 *
 * COMP90041, Sem2, 2022: Final Project
 * @author: Thomas Carey (831811)
 */
public class InvalidCharacteristicException extends Exception{
	
	/** 
	 * This constructor is invoked when no arguement is passed in to create a InvalidCharacteristicException object and to print the 
	 * default message of "WARNING: invalid characteristic in file"
	 */
    public InvalidCharacteristicException() {
		super("WARNING: invalid characteristic in file");
	}

	/**
	 * When an arguement is passed into InvalidCharacteristicException constructor this is invoked to enable a custom message to be 
	 * printed when this exception is thrown.
	 * @param message A String message to print when the exception is thrown.
	 */ 
    public InvalidCharacteristicException(String message) {
		super(message);
	}

}
