import java.io.File;
import java.util.Properties;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JOptionPane;

public class FolderMaker {

	public static int readNextNumber() {
		return 0;
	}
	
	public static void writeNextNumber(int n) {
	}
	
	public static String readParentPath() {
		return "C:\\Users\\tptam\\Documents";
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
	
	public static String removeBadChars(String path) {
		return path;
	}
	
	public static void main(String[] args) {
		int nextNumber = readNextNumber();
		String path = readParentPath();
		String clipboardText = getClipboardText();
		
		String newDirPath = nextNumber + "_" + clipboardText;
		newDirPath = removeBadChars(newDirPath);
		
		newDirPath = JOptionPane.showInputDialog(null, "新しいフォルダー名を入力してください。", newDirPath);
		newDirPath = removeBadChars(newDirPath);
		
		File directory = new File(path, newDirPath);
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
read file
set next number as the 1st line of the file
set parent dicrectory as the 2nd line of the file
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
