import java.io.File;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FolderMaker {
	static Preferences prefs = Preferences.userNodeForPackage(FolderMaker.class);
	
	public static String getPreference(String propertyName) {
		String result = prefs.get(propertyName, "NO DATA");
		return result;
	}
	
	public static void setPreference(String propertyName, String value) {
		prefs.put(propertyName, value);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			JOptionPane.showMessageDialog(null, "設定を書き込めませんでした。");
		}
	}

	public static int readNextNumber() {
		String s = getPreference("nextNumber");
		int n = -1;
		try {
			n = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "連番が不正です。");
		}
		return n;
	}
	
	public static void writeNextNumber(int n) {
		setPreference("nextNumber", String.valueOf(n));
	}
	
	public static String readParentPath() {
		return getPreference("parentPath");
	}
		
	public static String getClipboardText() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Clipboard cb = kit.getSystemClipboard();
		try {
			return (String) cb.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			return "";
		} catch (IllegalStateException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}
	
	public static String removeBadChars(String dirName) {
		char[] badChars = { ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', 
							'+', ',', '.', '/', ':', ';', '<', '=', '>', '?', 
							'[', '\\', ']', '^', '`', '{', '}', '|', '~'
		};
		StringBuilder sb = new StringBuilder();
		onDirName : for (int i = 0; i < dirName.length(); i++) {
			for (char c : badChars) {
				if (dirName.charAt(i) == c) {
					continue onDirName;
				}
			}
			sb.append(dirName.charAt(i));
		}
		
		return sb.toString();
	}
	
	
	public static void setAllPreferences(int nextNumber, String parentPath) {
		JFileChooser chooser;
		String s = JOptionPane.showInputDialog(null, "次の番号を入力してください", nextNumber);
		try {
			nextNumber = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "番号が不正です");
		}
		
		if (parentPath.equals("")) {
			chooser = new JFileChooser();
		} else {
			chooser = new JFileChooser(parentPath);
		}
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int selected = chooser.showDialog(null, "親フォルダを選択");
		if (selected == JFileChooser.APPROVE_OPTION) {
			parentPath = chooser.getSelectedFile().toString();
		}
		
		setPreference("nextNumber", String.valueOf(nextNumber));
		setPreference("parentPath", parentPath);
	}
	
	
	public static void main(String[] args) {
		
		int nextNumber = readNextNumber();
		String parentPath = readParentPath();
		
		if (nextNumber == -1 || parentPath.equals("NO DATA")) {
			setAllPreferences(0, "");
			nextNumber = readNextNumber();
			parentPath = readParentPath();
		} else if (args.length > 0 && args[0].equals("-config")) {
			setAllPreferences(nextNumber, parentPath);
			nextNumber = readNextNumber();
			parentPath = readParentPath();
		}
		
		String clipboardText = getClipboardText();
		
		String newDirName = nextNumber + "_" + clipboardText;
		newDirName = removeBadChars(newDirName);
		
		newDirName = JOptionPane.showInputDialog(null, "新しいフォルダー名を入力してください。", newDirName);
		newDirName = removeBadChars(newDirName);
		File directory = new File(parentPath, newDirName);
		if (directory.mkdirs()) {
			JOptionPane.showMessageDialog(null, "フォルダーを作成しました。");
			nextNumber++;
			writeNextNumber(nextNumber);
		} else {
			JOptionPane.showMessageDialog(null, "フォルダーの作成に失敗しました。");
		}
	}
}

/*
get nextNumber value from preferences and assign to variable nextNumber
get parentPath value from preferences and assign to variable parentPath

if nextNumber or parentPath is invalid then
  let user modify preferences
  get nextNumber value from preferences and assign to variable nextNumber
  get parentPath value from preferences and assign to variable parentPath
else if the program's first argument is -config then
  let user modify preferences
  get nextNumber value from preferences and assign to variable nextNumber
  get parentPath value from preferences and assign to variable parentPath
show input dialog
put next number + " " + what is in the clipboard in the input box
if user clicks OK, 
 try to create a directory with the same name as the input 
 if successful
   show message dialog that informs success
   increment next number
   write next number on the first line of the file
 otherwise
   show message dialog that informs failure
*/


//https://www.javatpoint.com/properties-class-in-java