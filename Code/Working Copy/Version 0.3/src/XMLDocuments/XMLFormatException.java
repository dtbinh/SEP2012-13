package XMLDocuments;

public class XMLFormatException extends Exception
{
    private String line;
    
    public XMLFormatException(String line, String msg)
    {
	super(msg);
	this.line= line;
    }
    
    
    public String toString()
    {
	return line+":0:"+super.toString();
    }
}
