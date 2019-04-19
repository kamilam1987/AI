package ie.gmit.sw.ai.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class GameFuzzyLogic {
	
	public double PlayerHealth(int weapon , int enemy) {
		FIS fis = FIS.load("resources/fuzzy/engage.txt", true); //Load and parse the FCL
		//JFuzzyChart.get().chart(fis);
		
		//fis.chart(); //Display the linguistic variables and terms
		fis.setVariable("weapon", weapon); //Apply a value to a variable
		fis.setVariable("enemy", enemy);
		fis.evaluate(); //Execute the fuzzy inference engine
		//FuzzyChart.get().chart(fis.getVariable("risk").getDefuzzifier(),"Risk",true);
		return fis.getVariable("risk").getValue(); //Output end result
	}

}
