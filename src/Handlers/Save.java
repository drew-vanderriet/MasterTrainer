package Handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Entity.Creature;

public class Save {
/*
 * Data to be saved:
 * + player creature level stats
 * + player creature experience
 * 
 * A Save object represents a set creature.
 * It then 
 * 
 */
	private Creature creature;	// Creature to be loaded/saved
	private String saveFileAdd;	// address of save file

	private boolean loadFail;

	public Save(Creature c, String s) {
		creature = c;
		saveFileAdd = s;
		
		File folder = new File("Saves");
	    if (!folder.exists()) {
	       folder.mkdirs();
	    }

		File f = new File(s);
		System.out.println("Save File path: " + f.getPath());
		System.out.println("File exists: " + f.exists() + "\nFile directory: " + f.isDirectory());
		if(f.exists() && !f.isDirectory()) { 
			loadFail = false; 
		} else { loadFail = true; } 
//		loadFail = false;
	}

	public void saveData(ArrayList<Data> data) {

		try{  // Catch errors in I/O if necessary.
			// Open a file to write to, named SavedObj.sav.
			FileOutputStream saveFile = new FileOutputStream(saveFileAdd);
//			FileOutputStream saveFile = new FileOutputStream(getClass().getResource(saveFileAdd).getPath());
			// Create an ObjectOutputStream to put objects into save file.
			ObjectOutputStream save = new ObjectOutputStream(saveFile);

			// Now we do the save.
			//==============================
			// save the names as String[] and type as int[]
			String[] nameOfData = new String[data.size()];
			int[] typeOfData = new int[data.size()];
			for(int i = 0; i < data.size(); i++) {
				nameOfData[i] = data.get(i).getName();
				typeOfData[i] = data.get(i).getType();
			}
			save.writeObject(nameOfData);
			save.writeObject(typeOfData);
			
			// save the data
			for(int i = 0; i < data.size(); i++) {
				switch (data.get(i).getType()) {
				case Data.DOUBLE:
					save.writeObject(data.get(i).getData(Data.T_DOUBLE));	
					break;
				case Data.INT:
					save.writeObject(data.get(i).getData(Data.T_INT));	
					break;
				case Data.STRING:
					save.writeObject(data.get(i).getData(Data.T_STRING));
					break;
				default: 
					System.out.println("Save Creature Data error:\n"
							+ "Invalid data type.");
					break;
				}
			}
			// Close the file.
			save.close(); // This also closes saveFile.
		}
		catch(Exception exc){
			exc.printStackTrace(); // If there was an error, print the info.
		}

	}
	
	public ArrayList<Data> loadData() {
		ArrayList<Data> data = new ArrayList<Data>();
		
		if(loadFail) { return data; } // can't load file as doesn't exist
		// Wrap all in a try/catch block to trap I/O errors.
		try{
			// Open file to read from, named SavedObj.sav.
			FileInputStream saveFile = new FileInputStream(saveFileAdd);
//			FileInputStream saveFile = new FileInputStream(getClass().getResource(saveFileAdd).getPath());

			// Create an ObjectInputStream to get objects from save file.
			ObjectInputStream save = new ObjectInputStream(saveFile);

			// Now we do the restore.
			// readObject() returns a generic Object, we cast those back
			// into their original class type.
			// For primitive types, use the corresponding reference class.
			
			// loading header info
			String[] nameOfData = (String[]) save.readObject();
			int[] typeOfData = (int[]) save.readObject();
			
			// loading and constructing Data array.
			for(int i = 0; i < nameOfData.length; i++) {
				switch (typeOfData[i]) {
				case Data.DOUBLE:
					data.add(new Data(nameOfData[i], (double) save.readObject()));
					break;
				case Data.INT:
					data.add(new Data(nameOfData[i], (int) save.readObject()));
					break;
				case Data.STRING:
					data.add(new Data(nameOfData[i], (String) save.readObject()));
					break;
				default: 
					System.out.println("Load Creature Data error:\n"
							+ "Invalid data type received.");
					break;
				}
			}

			// Close the file.
			save.close(); // This also closes saveFile.
		}
		catch(Exception exc){
			exc.printStackTrace(); 	// If there was an error, print the info.
		}

		return data;
	}

	public boolean loadFailed() { return loadFail; }

}
