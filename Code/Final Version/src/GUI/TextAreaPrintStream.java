package GUI;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
/**  TextAreaPrintStream class is set out put of system.out.println() method, textArea
*
* @author Li Shikai
* @version 3.0 build 18/10/2012.
*/

public class TextAreaPrintStream extends PrintStream {

	public TextAreaPrintStream(JTextArea ta) {
		super(new TextAreaOutputStream(ta));
	}
}

class TextAreaOutputStream extends OutputStream {
	private JTextArea ta;
	
	public TextAreaOutputStream(JTextArea ta) {
		this.ta = ta;
	}
	/**  
	*
	*@param b set current textArea caret position on the end of textArea
	*/
	public void write(int b) {
		char c = (char) b;
		ta.append(String.valueOf(c));
		int length = ta.getText().length(); 
		ta.setCaretPosition(length);
	}
}