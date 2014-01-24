package GUI;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;


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
	
	public void write(int b) {
		char c = (char) b;
		ta.append(String.valueOf(c));
		int length = ta.getText().length(); 
		ta.setCaretPosition(length);
	}
}