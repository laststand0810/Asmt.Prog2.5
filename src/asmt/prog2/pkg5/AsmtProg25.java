/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asmt.prog2.pkg5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author nguyenpham
 */
public class AsmtProg25 {
    static HashSet<String> hashSet = new HashSet<>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner fileIn;
        TreeSet<String> suggestions = new TreeSet<>();
        try {
            fileIn = new Scanner(new File("src/asmt/prog2/pkg5/words.txt"));
            // put words in words.txt to hashSet
            while (fileIn.hasNext()){
                hashSet.add(fileIn.next().trim().toLowerCase());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsmtProg25.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //let user input file
        try {
            String temp;
            fileIn = new Scanner(getInputFileNameFromUser()).useDelimiter("[^a-zA-Z]+");
            while (fileIn.hasNext()){
                temp = fileIn.next().toLowerCase();
                suggestions = corrections(temp, hashSet);
                System.out.println(temp + ": " + suggestions.toString());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsmtProg25.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            System.out.println("User have not chosen any file");
        }
        
        
        
    }

    static File getInputFileNameFromUser(){
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Select file for input");
        int option = fileDialog.showOpenDialog(null);
        if (option != JFileChooser.APPROVE_OPTION){
            return null;
        }else{
            return fileDialog.getSelectedFile();
        }
    }
    
    static TreeSet corrections (String badWord, HashSet dictionary){
        String alphabet = "abcdefghijklmnopqrstuvwxyz ";
        TreeSet<String> result = new TreeSet<>();
        String preString, modification = "", postString, bufferString;
        boolean lastCharChecked = false;
        
        for(int i = 0; i<badWord.length(); i++){
            
            preString = badWord.substring(0, i);
            postString = badWord.substring(i+1, badWord.length());
            

            // check delete any one of the letters
            bufferString = preString + postString;
            if (hashSet.contains(bufferString)) 
                result.add(bufferString);
            
            
            for (char a: alphabet.toCharArray()){
                if (a != " ".charAt(0)){
                    // check adding a letter
                    modification = badWord.charAt(i) + a + "";
                    bufferString = preString + modification + postString;
                    if (hashSet.contains(bufferString)) 
                        result.add(bufferString);

                    // check adding letter behind the string
                    if (!lastCharChecked){
                        if (hashSet.contains(badWord + a)) 
                            result.add(bufferString);
                        lastCharChecked = true;
                    }
                    // check changing the letter
                    // exclude the letter itself
                    if (a == badWord.charAt(i)) continue;


                    // otherwise, carry on
                    modification = a + "";
                    bufferString = preString + modification + postString;
                    if (hashSet.contains(bufferString)) 
                        result.add(bufferString);
                }else{
                
                    result.addAll(corrections(preString + badWord.charAt(i), dictionary));
                    if(i<=badWord.length())
                        result.addAll(corrections(postString, dictionary));
                }
                
                
            }
            
        }
         return result;    
    }
    
    static boolean dictionaryCheck(String check){
        return hashSet.contains(check);
    }
}
