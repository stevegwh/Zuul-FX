package command.game.eventOutput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import command.ICommandOutput;
import view.IOHandler;

/**
 * Used to output to the user the beginning of the game.
 * 
 * @author Steve
 *
 */
public class GameStartOutput implements ICommandOutput {

	@Override
	public void init(String[] inputArray) {
        // The name of the file to open.
		String fileName = "zuul_title.txt";
        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            InputStreamReader fileReader = new InputStreamReader(this.getClass().getResourceAsStream(fileName));

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                IOHandler.output.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}

}
