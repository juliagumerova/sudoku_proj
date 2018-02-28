/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Julia
 */
public class Sudoku {
    
    private static final int LINE_SIZE = 9;
    private static final int QUADRANT_SIZE = 3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        char[][] grid = createGridFromFile("input/input_sudoku.txt");
        printGrid(grid);
        
        try {
            validateRows(grid);

            validateColumns(grid);
        
            validateQuadrants(grid);
            
        } catch (SudokuValidationException e) {
            System.out.println("Sudoku is NOT valid");
            System.out.println(e.getMessage());
            System.exit(0);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } 
        
        System.out.println("Sudoku is valid");
    }
    
    private static void validateQuadrants(char[][] grid) throws Exception {
        
         for (int startRow = 0; startRow <= LINE_SIZE - QUADRANT_SIZE; startRow = startRow + QUADRANT_SIZE) {
            for (int startCol = 0; startCol <= LINE_SIZE - QUADRANT_SIZE; startCol = startCol + QUADRANT_SIZE) { 
                
                if (!isQuadrantValid(grid, startRow, startCol) ) {
                    throw new SudokuValidationException("Quadrant ["+startRow +", "+startCol+"] is invalid");    
                }
            }          
        }
    }
    
    private static boolean isQuadrantValid(char[][] grid, int startRow, int startCol) {
        
        char quad [] = new char [LINE_SIZE];
        
        int n = 0;
        
        for (int i = startRow; i < startRow + QUADRANT_SIZE; i++ ) {
            for (int j = startCol; j < startCol + QUADRANT_SIZE; j++ ) {
                quad [n++] = grid[i][j];
            }
        }
        
        return isGridPartValid(quad);
    }  
        
    private static void validateColumns(char[][] grid) throws Exception {

        for (int colNum = 0; colNum < LINE_SIZE; colNum++) {           
            if (!isColValid(grid, colNum)) {
                throw new SudokuValidationException("Col "+colNum+1+" is invalid");    
            }
        }
    }
    
    private static boolean isColValid(char[][] grid, int colNum) throws Exception {
        
        char col [] = new char [LINE_SIZE];
        
        for (int rowNum = 0; rowNum < LINE_SIZE; rowNum ++) {
            
            col [rowNum] = grid[rowNum][colNum];
        }
       
        if (!isGridPartValid(col)) {
            return false;    
        }
           
        return true;
    }
    
    private static void validateRows(char[][] grid) throws Exception {
        
        for (char[] row : grid) {           

            if (!isGridPartValid(row)) {
                throw new SudokuValidationException("Row "+Arrays.toString(row)+" is invalid");    
            }
        }
    }
    
    
    
    private static boolean isGridPartValid(char[] line) {
        
        Set validated = new HashSet();
        
        for (int i = 0; i < LINE_SIZE - 1; i++) {
            validated.add(line[i]);
            
            if (validated.contains(line[i+1])) {
                return false;
            }
        }

        return true;
    }
    
    public static char[][] createGridFromFile(String filename) throws Exception {
        char[][] grid = new char[LINE_SIZE][LINE_SIZE];
        File inFile = new File(filename);
        Scanner in = new Scanner(inFile);

        char[] lineChar = new char[LINE_SIZE];
        int lineCount = 0;

        while (in.hasNextLine()) {
           
            String line = in.nextLine().trim();
            
            lineChar = line.toCharArray();
            
            int rowSize = lineChar.length;
            
            if (rowSize != LINE_SIZE) {
                throw new SudokuValidationException("Row size is not " +LINE_SIZE);
            }
                
            for (int i=0; i< rowSize; i++) {
                
                if (!Character.isDigit(lineChar[i])) {
                    throw new SudokuValidationException("Character is not a digit");
                }
                
                if (Character.getNumericValue(lineChar[i]) < 1 || Character.getNumericValue(lineChar[i]) > 9) {
                    throw new SudokuValidationException("Character is not a digit between 1 and 9");
                }
                  
                grid[lineCount][i] = lineChar[i];
            }
            
            lineCount++;
        }

        in.close();

        if (lineCount != LINE_SIZE) {
            throw new SudokuValidationException("Column size is not " +LINE_SIZE);
        }
        
        return grid;
    }
    
    private static void printGrid(char[][]grid) {
        
        for(char[] row : grid) {
            printRow(row);
        }
    }
    
    public static void printRow(char[] row) {
        for (char i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }

}
