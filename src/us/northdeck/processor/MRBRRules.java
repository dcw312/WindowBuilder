package us.northdeck.processor;

public class MRBRRules {
	class table {
		String file;
		String type;
		rule[] rules;
	}
	
	class rule {
		whereUsed[] whereUseds;
		String[] carriers;
		setType[] setTypes;
		setReq[] setReqs;
	}
	
	class whereUsed {
		String path;
	}
	class carrier {}
	class setType {
		String path;
		String type;
	}
	class sourceSystem {}
	class switchRule {
		String xml;
	}
	class caseRule {}
	class value {}
	class setReq {
		final static int REQ = 0;
		final static int OPT = 1;
		final static int DIS = 2;
		int to;
		String path;
	}
	
	class setOneOf {}
	
	public static table newTable() {
		MRBRRules r = new MRBRRules();
		table table = r.new table();
		rule rule = r.new rule();
		rule.whereUseds = new whereUsed[1];
		rule.whereUseds[0].path = "/PostpaidPlansRequest";
		
		rule.carriers = new String[1];
		rule.carriers[0] = "VEZ"; 
		
		table.rules = new rule[1];
		table.rules[0] = rule;
		
		rule.setTypes = new setType[1];
		rule.setTypes[0].type = "vezSIM";
		rule.setTypes[0].path = "SIM";
		
		rule.setReqs = new setReq[1];
		rule.setReqs[0].to = setReq.REQ;
		
		return table;
		
	}
}
