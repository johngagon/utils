package jhg.diviner;

import java.util.Random;

public class Diviner {

	//exempt words: If, When, about, according, be, about, as, then, therefore, (most adverbs that are not ly and conj),
	// than
	//  who, what, when, where, why,  how, how come, which, whichever, whatever, wherever, whenever, 
	// because, but, or, and, though, even
	// while, again, ever, here there, now, still, well, almost, enough, even, quite, so, very, during, just as
	// tomorrow, yesterday, please, yes, no   "not" also
	//  can could would should will will not, won't, may, front, back 
	//other exempts: I, you, my, mine, me, one, like, we, our its their, them, 
	
	
	//singular
	private static String[] titles = {"king","queen","prince","knight","duke","duchess","baron","baroness"};
	private static String[] occupations = {"doctor","banker","ruler","judge","criminal","professor","bishop","policeman","fireman"};
	private static String[] tools = {"wand","hammer","scepter","measuring rod"};
	private static String[] bodypart = {"hand","mouth","eyes","head","feet","face","legs","arms"};//symbolic
	private static String[] disasters = {"volcano","earthquake","whirlwind"};
	private static String[] food = {"potato","pie","fruit","melon","cheese","butter","beef","milk","nuts"};
	private static String[] generalThings = {"country","time","place","reason","person","thing","start","end","plants","animals","money","supply"};
	private static String[] countries = {"Africa","England","France","Italian"};
	private static String[] cities = {"London","Brussels","Rome","Geneva","Madrid","New York"};
	private static String[] dirs = {"north","east","south","west"};
	private static String[] times = {"second","minute","hour","day","week","month","season","year","decade","century","millenia","night"};
	private static String[] emotions = {"happiness","sadness","anger","fear"};
	private static String[] positions = {"beginning","end"};
	private static String[] abstractions = {"arguments","questions"};
	private static String[] relation = {"mother","father","sister","brother","relatives","grandfather","grandmother","friend","enemy","lover","husband","wife","boss","teacher","student","workers"};
	private static String[] basics = {"snow","rain","rock","water","sand","gold","gems"};
	private static String[] buildings = {"church","school","court","office","park","theatre","stadium","bank","hospital","police station","fire station"};
	private static String[] symbolic_animals = {"lion","elephant","tiger","turtle","snake","eagle","bear"};
	private static String[] surnames = {"Smith","Jones","Williams","Taylor","Brown","Davies","Evans","Wilson","Thomas","Johnson"};
	private static String[][] nouns = {titles,occupations,tools,bodypart,disasters,food,generalThings,countries,cities,dirs,times,emotions,positions,relation,abstractions,basics,buildings,symbolic_animals,surnames};
	//pluaral
	private static String[] lesser = {"servants","nurses","slaves"};
	private static String[] family = {"children","ancestors","posterity"};
	private static String[] farm_animals = {"horses","cows","sheep","hogs"};
	
	private static String[] bodypartsP = {"hands","mouths","eyes","heads","feet","faces","legs","arms"};
	private static String[] clothingP = {"cloaks","shirts","pants","vests","hats","shoes","coats","gloves"};
	private static String[] generalThingsP = {"countries","times","places","reasons","people","things","way","advice","actions","thoughts","feelings","words","numbers"};
	private static String[] timesP = {"seconds","minutes","hours","days","weeks","months","seasons","years","decades","centuries","millenias"};
	private static String[][] nounp = {lesser,family,farm_animals,bodypartsP,clothingP,generalThingsP,timesP};
	
	//adjectives
	private static String[] colors = {"red","orange","chartreuse","green","teal","turquoise","azure","blue","violet","magenta","pink","black","white","grey","silver","gold","copper","iron","brown"};
	private static String[] temperatures = {"hot","cold","freezing","chilly","warm","scorching"};
	private static String[] countriesJ = {"African","English","French","Italian"};
	private static String[] adj = {"great","low","high","same","different","large","important","trivial","public","private","able","little","much","few","many","big","small","few","many","good","bad","right","wrong","true","false","first","last","hot","cold","previous","cheap","expensive","next","new","long","short","old","young","beautiful","ugly","least","most"};
	private static String[] emotionalJ = {"happy","sad","angry","scared"};
	private static String[] dirsJ = {"northern","easter","southern","western"};
	private static String[] quantities = {"one","two","three","half","four","five","six","seven","eight","nine","ten","fifteen","twenty","twenty-five","fifty","one-hundred","several hundred","several","thousand","thousdands","hundreds","tens"};
	//pure, metalic, liquid, gaseous
	private static String[][] adjectives = {colors,temperatures,countriesJ,adj,emotionalJ,dirsJ,quantities};
	

	
	
	private static String[] verbsPx = {"be","place","accompany","come","go","get","keep","let","make","put","seem","take","do","have","say","send","look","think","see","like","want","need","wish","work","use","start","end","finish","continue","move","walk","run","sit","stay","give","believe","know","understand","call","find","tell","ask","feel","try","fail","succeed","leave","rise","fall"};
	private static String[] verbsD = {"was","placed","accompanied","came","went","got","kept","let","made","put","seemed","taken","did","had","said","sent","looked","thought","saw","liked","wanted","needed","wished","worked","used","started","ended","finished","continued","moved","walked","ran","sat","stayed","gave","believed","knew","understood","called","found","told","asked","felt","tried","failed","succeeded","left","risen","fell"};//past tense
	private static String[] verbsP = {"is","places","accompanies","comes","goes","gets","keeps","lets","makes","puts","seems","takes","does","has","says","sends","looks","thinks","sees","likes","wants","needs","wishes","works","uses","starts","ends","finishes","continues","moves","walks","runs","sits","stays","gives","believes","knows","undertands","calls","finds","tells","asks","feels","tries","fails","succeeds","leaves","rises","falls"};//present tense a
	private static String[] verbsPg = {"being","placing","accompanying","coming","going","getting","keeping","letting","making","putting","seeming","taking","doing","having","saying","sending","looking","thinking","seeing","liking","wanting","needing","wishing","working","using","starting","ending","finishing","continuing","moving","walking","running","sitting","staying","giving","believing","knowing","understanding","calling","finding","telling","asking","feeling","trying","failing","succeeding", "leaving","rising","falling"};
	
	private static String[][] verbs = {verbsPx,verbsD,verbsP,verbsPg};
	
	/* kill  treat heal
	 * poison 	  sell  buy pay  eat own  
	 * drink  	  smell taste
	 * accuse  defend  attack
	 * act  advise  reply
	 */
	
	
	
	//other parts
	
	
	private static String[] pron = {"one","all","none","both","either","neither","any","every","no","other","some","such","this","that","those","these","he","she","it"};//him,his
	private static String[] preps = {"by","of","for","to","among","from","with"};
	private static String[] prepsT = {"at","after","before","until"};
	private static String[] prepsP = {"at","on","in","into","between","across","against","down","off","over","out","through","under","up","together"};
	private static String[] articles = {"a","the","its","this","that"};//before noun
	private static String[] must = {"will","shall"};
	private static String[] adverbsLy = {"quickly", "slowly"};	
	
	private static String[][] speechies = {pron,preps,prepsT,prepsP,articles,must,adverbsLy};
	
	private static String[][][] words = {nouns,nounp,verbs,adjectives,verbs,speechies};

	private static String template = "%1$s %2$s %3$s %4$s %5$s ";
	

	
	
	
	/*
	 * When the Ns is Vd P the Nobj
	 * and the Np are Vd P the Nobj
	 * The Adj N will/shall be Vd P the/its Nobj.
	 * At this time the Np and the Np will/shall Vp Adv
	 * 
	 * When the Title of Np Vs a Nobj
	 * Then shall all the Np V at the Nlocation.
	 * 
	 */
	
	public static void test2(){
		for(int i=0;i<=50;i++){
			String s1 = d3(words);
			String s2 = d3(words);
			String s3 = d3(words);
			String s4 = d3(words);
			String s5 = d3(words);
			
			System.out.println( String.format(  template,s1,s2,s3,s4,s5  ) ) ;
		}
	}
	
	public static void test(){
		String template1 = "When the %1$s is %2$s %3$s the %4$s \n"  //%10$s
				+"and the %5$s are %6$s %7$s the %8$s \n"
				+"the %9$s %10$s %11$s be %12$s %13$s %14$s %15$s \n"
				+"at this time the %16$s and the %17$s %18$s be %19$s %20$s"
				;
		
		System.out.println( String.format(template1, Ns(), Vd(), P(), Ns()
				, Np(), Vd(), P(), Ns()
				, J(), Ns(), M(), Vd(), P(), A(), Ns()
				, Np(), Np(), M(), Vd(), Vl()) );
	}
	
	public static void main(String[] args){
		test2();
	}
	
	
	public static void simpletest(){
		//http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html
		String template = "When the %1$s is %2$s,  ";
		System.out.println(String.format(template, 21, "x"));		
	
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}	

	public static String M(){
		return d1(must);
	}
	public static String Ns(){
		return d2(nouns);
	}
	public static String Np(){
		return d2(nounp);
	}
	public static String Vl(){
		return d1(adverbsLy);
	}
	public static String Vd(){
		return d1(verbsD);
	}
	public static String Vp(){
		return d1(verbsP);
	}
	public static String P(){
		return d1(preps);
	}
	public static String A(){
		return d1(articles);
	}
	public static String J(){
		return d2(adjectives);
	}

	private static String d3(String[][][] sArrrr){
		int sArrrrMax = sArrrr.length-1;
		int sArrrrI = randInt(0,sArrrrMax);
		String[][] sArrr = sArrrr[sArrrrI];
		return d2(sArrr);
	}
	
	private static String d2(String[][] sArrr){
		int sArrrMax = sArrr.length-1;
		int sArrrI = randInt(0,sArrrMax);
		String[] sArr = sArrr[sArrrI];
		return d1(sArr);
	}
	
	private static String d1(String[] sArr){
		int sArrMax = sArr.length-1;
		int sArrI = randInt(0,sArrMax);
		String val = sArr[sArrI];
		return val;		
	}
	
	
	
	public static String randomName(){
		String[] initials = {"Walden","Tom","Peter","","John","William","David","Will","Robert","Edward"};
		String[] finals = {"meyer","berg","ford","smith","son","stein","s"};
		return d1(initials)+d1(finals);
	}
	public static String randomInitials(){
		String[] lettersI = {"A","B","C","D","E","F","G","H","J","K","L","M","N","P","R","S","T","V","W","Z"};
		String[] lettersF = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		return d1(lettersF)+d1(lettersF);
		
	}		
	
}
/*

	//verbs
	private static String[] sverbS = {"is"};
	private static String[] sverbP = {"are"};
	private static String[] sverbSp = {"was"};
	private static String[] sverbPp = {"were"};
*/