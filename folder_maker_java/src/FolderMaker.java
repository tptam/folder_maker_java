import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JOptionPane;

public class FolderMaker {

	public static String readProperty(String propertyName) {	
		String result = "";
		try {
			InputStream in = FolderMaker.class.getClassLoader().getResourceAsStream("config.properties");
			Properties p=new Properties(); 
			p.load(in);
			result = p.getProperty(propertyName);
		} catch (FileNotFoundException e) {
			// is this still needed?
			JOptionPane.showMessageDialog(null, "設定ファイルが見つかりません。");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "設定の読み込みに失敗しました。");
			e.printStackTrace();
		}
	    return result;
	}
	
	public static void writeProperty(String propertyName, String value) {
		String fileName = "config.properties";
		try {
			InputStream in = FolderMaker.class.getClassLoader().getResourceAsStream(fileName);
			Properties p=new Properties(); 
			p.load(in);
			p.setProperty(propertyName, value);
			FileOutputStream out = new FileOutputStream("xyz.properties");
			p.store(out , null);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "設定ファイルが見つかりません。");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "設定の書き込みに失敗しました。");
			e.printStackTrace();
		}
	}
	
	public static int readNextNumber() {
		String s = readProperty("nextNumber");
		int n = 0;
		try {
			n = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "連番が不正です。");
		}
		return n;
	}
	
	public static void writeNextNumber(int n) {
		writeProperty("nextNumber", String.valueOf(n));
	}
	
	public static String readParentPath() {
		return readProperty("parentPath");
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


//https://www.javatpoint.com/properties-class-in-java