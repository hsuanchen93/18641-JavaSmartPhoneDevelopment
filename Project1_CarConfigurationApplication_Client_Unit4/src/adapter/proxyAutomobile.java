/*
 * Hsuan Chen (hsuanc)
 * 
 * CreateAuto - BuildAuto, printAuto
 * UpdateAuto - updateOptionSetName, updateOptionPrice, deleteOptionSet, 
 * 				deleteOption, setOptionChoice
 * FixAuto - fix
 * InfoAuto - modelExist, printAutoHashMap, printChoices, getTotalPrice
 * EditAuto - editOptionPrice
 * 
 */

package adapter;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

import exception.AutoException;
import model.*;
import scale.EditOptions;
import util.FileIO;

public abstract class proxyAutomobile {
	//private static Automobile a1;
	private static LinkedHashMap<String,Automobile> autoLHashMap = new LinkedHashMap<String,Automobile>();
	
	/* CreatAuto */
	//BuildAuto
	public void BuildAuto(String filename) {
		try {
			Automobile a1 = new FileIO().buildAutoObject(filename);
			autoLHashMap.put(a1.getModel(), a1);
		} catch (AutoException e) {
			//Fix exception 5: invalid filename
			if(e.getErrorno()==5) {
				FixAuto f = new BuildAuto();
				filename = f.fix(e.getErrorno());
				BuildAuto(filename);
			}
		} catch (FileNotFoundException e) { }
	}
	//printAuto
	public void printAuto(String modelName) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).print();
		}
	}
	//addAuto
	public void addAuto(Automobile auto){
		autoLHashMap.put(auto.getModel(), auto);
	}
	
	
	/* UpdateAuto */
	//updateOptionSetName
	public void updateOptionSetName(String modelName, String optionSetName, String newName) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).updateOptionSet(optionSetName, newName);
		}
	}
	//updateOptionPrice
	public void updateOptionPrice(String modelName, String optionSetName, String optionName, double newPrice) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).updateOption(optionSetName, optionName, optionName, newPrice);
		}
	}
	//deleteOptionSet
	public void deleteOptionSet(String modelName, String optionSetName) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).deleteOptionSet(optionSetName);
		}
	}
	//deleteOption
	public void deleteOption(String modelName, String optionSetName, String optionName) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).deleteOption(optionSetName, optionName);
		}
	}
	//setOptionChoice
	public void setOptionChoice(String modelName, String optsetName, String optName) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).setOptionChoice(optsetName, optName);
		}
	}
	
	
	/* FixAuto */
	//fix
	public String fix(int errno) {
		return exception.AutoException.fix(errno);
	}
	
	
	/* InfoAuto */
	//modelExist
	public boolean modelExist(String modelName) {
		if(autoLHashMap.containsKey(modelName)) {
			return true;
		}
		else {
			try {
				throw new AutoException(1, "Automobile Does Not Exist");
			} catch (AutoException e) { }
		}
		return false;
	}
	//printAutoHashMap
	public void printAutoHashMap() {
		Set<String> st = autoLHashMap.keySet();
		Iterator<String> itr = st.iterator();
	    while (itr.hasNext()){
	      printAuto(itr.next());
	    }
	}
	//printChoices
	public void printChoices(String modelName) {
		if(modelExist(modelName)) {
			autoLHashMap.get(modelName).printChoice();
		}
	}
	//getTotalPrice
	public double getTotalPrice(String modelName) {
		if(modelExist(modelName)) {
			return autoLHashMap.get(modelName).getTotalPrice();
		}
		return -1;
	}

	
	/* EditAuto */
	//editOptionSetName
		public void editOptionSetName(String modelName, String optionSetName, String newName) {
			if(modelExist(modelName)) {
				String[] args = new String[]{optionSetName, newName};
				EditOptions editThread = new EditOptions(autoLHashMap.get(modelName), "editOptionSetName", args);
				editThread.start();
			}
		}
	//editOptionPrice
	public void editOptionPrice(String modelName, String optionSetName, String optionName, double newPrice) {
		if(modelExist(modelName)) {
			String[] args = new String[]{optionSetName, optionName, String.valueOf(newPrice)};
			EditOptions editThread = new EditOptions(autoLHashMap.get(modelName), "editOptionPrice", args);
			editThread.start();
		}
	}
	
}
