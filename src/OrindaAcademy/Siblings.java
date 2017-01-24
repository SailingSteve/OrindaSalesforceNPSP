package OrindaAcademy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * De-dupe the foreign keys for Salesforce, Two kids from one family use the name of the first kid to 
 * attend the school's name (in the raw format from the sis).   The raw format looks like "Podell; Steve"
 * Some of these corrections on this list are actually spelling corrections -- assume the latest year has the 
 * best data.
 * 
 * @author stevepodell
 */
public class Siblings {
			
	public static final Map<String, String> map;
	 
	static {
	  Map<String, String> m = new HashMap<String, String>();  
	  m.put("Agramont; Gabriela","Agramont; Elizabeth");
	  m.put("Agramont; Sebastian","Agramont; Elizabeth");
	  m.put("Agramont; Sergio","Agramont; Elizabeth");
	  m.put("Akazawa; Niamh","Akazawa; Malia");
	  m.put("Bakos; Torin","Bakos; Dylan");
	  m.put("Bell; Michael","Bell; Ithemba");
	  m.put("Bizjak; Maxx","Bizjak; Gina");
	  m.put("Bratt; Margaret","Bratt; Gerrit");
	  m.put("Cardona; Mariana","Cardona; Crystal");
	  m.put("Cardona; Marianna","Cardona; Crystal");
	  m.put("Cook; Zachary","Cook; Ben");
	  m.put("Dunfield; Christopher","Dunfield; Chris");
	  m.put("Etheridge; Riley","Etheridge; Marie");
	  m.put("Feitelberg; Jacob","Feitelberg; Daniel");
	  m.put("Fineman; Margarita","Fineman; Charlie");
	  m.put("Goldman; Hanna","Goldman; Dylan");
	  m.put("Hawkyard; Philip","Hawkyard; Katherine");
	  m.put("Haymes; Mason","Haymes; Ethan");
	  m.put("Kastenbaum; Steven","Kastenbaum; Andrew");
	  m.put("Kornguth; Lindsay","Kornguth; Gregory");
	  m.put("Marcus; Dylan","Marcus; Devin");
	  m.put("Ntekume; Blessing (Oker","Ntekume; Blessing (Okero");  
	  m.put("Owens; Sebleh","Owens; Seble");
	  m.put("Palacios; Raphael","Palacios; Gabriel");
	  m.put("Schmidt; Blair","Schmidt; Blaine");
	  m.put("Seeno; Sophia","Seeno; Dominic");
	  m.put("Sherman; Chris","Sherman; Christopher");
	  m.put("Shethar; Zarrah (Marina","Shethar; Zarrah (Marina)");
	  m.put("Smith; Alexandra","Smith; Alexandria");
	  m.put("Stover; Sean","Stover; Hayden");
	  m.put("Wang; Vicky (Yidan)","Wang; Yidan (Vicky)");
	  m.put("Wichner; Na'am","Wichner; Liam");
	  m.put("Wichner; Naomi","Wichner; Liam");
	  m.put("Widess; Andy","Widess; Andrew");
	  m.put("Yang; Chris (Chang Heng","Yang; Chris (Chang Heng)");
	  m.put("Yang; Chris Chang Heng","Yang; Chris (Chang Heng");
	  m.put("Yang; Megan","Yang; Hannah");

	  map = Collections.unmodifiableMap(m);
	}
}