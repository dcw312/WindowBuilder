package us.northdeck.xsomtest;

public class DecisionTable {
	public static String CONDITION = "condition";
	
	class Condition {
		String name;
		String expr;
		Scenario[] scenariosTests;
		Scenario[] scenariosActions;
		
	}
	class Scenario {
		boolean result = true;
		boolean include = false;
	}
	
}
