/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asmt.prog2.pkg5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
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
    static HashSet<String> dictionary = new HashSet<>();
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
                dictionary.add(fileIn.next().trim().toLowerCase());
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
                if (!dictionary.contains(temp)){
                    suggestions = corrections(temp, dictionary);

                    // Output result
                    if (!suggestions.isEmpty()){
                        System.out.println(temp + ": " + suggestions.toString());
                    }else{
                        System.out.println(temp + ": (no suggestion)");
                    }
                }
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
    
    /** corrections function will check the bad word with included dictionary
    * checks included: 
    *   delete from any one of the letter
    *   change any letter to another letter
    *   insert additional letter in between any position
    *   swap any two neighboring letter
    *   insert a blank space in between the letter
    */
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
            if (AsmtProg25.dictionary.contains(bufferString)) 
                result.add(bufferString);
            
            // check letter swap with the letter right after the position
            if(i+1 < badWord.length()){
                bufferString = preString + badWord.charAt(i+1) + badWord.charAt(i) + 
                        postString.substring(1, postString.length());
                if (AsmtProg25.dictionary.contains(bufferString)) 
                    result.add(bufferString);
            }
            
            for (char a: alphabet.toCharArray()){
                if (a != " ".charAt(0)){
                    // check adding a letter before the position
                    modification =a + badWord.charAt(i) + "";
                    bufferString = preString + modification + postString;
                    if (AsmtProg25.dictionary.contains(bufferString)) 
                        result.add(bufferString);

                    // check adding letter at the end of string
                    if (!lastCharChecked){
                        if (AsmtProg25.dictionary.contains(badWord + a)) 
                            result.add(badWord + a);
                        //only need to check once so set flag back to true
                        lastCharChecked = true;
                    }
                    // check changing the letter
                    // exclude the letter itself
                    if (a == badWord.charAt(i)) continue;


                    // otherwise, carry on
                    modification = a + "";
                    bufferString = preString + modification + postString;
                    if (AsmtProg25.dictionary.contains(bufferString)) 
                        result.add(bufferString);
                }else{
                    
                    // Check word separation
                    if(AsmtProg25.dictionary.contains(preString + badWord.charAt(i)) 
                            && AsmtProg25.dictionary.contains(postString)){
                        result.add(preString + badWord.charAt(i)+ " " + postString);
                    }   
                }   
            }            
        }
         return result;    
    }
}
