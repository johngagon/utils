package jhg.puzzles;

public class Me {
	public static void test(){
		System.out.println(
				Me.class.getName().replaceAll(".","/")+".class" //wow, did this earlier.
				);
		
		/*
		 * naive:		jhg/puzzles/Me.class
		 * expected:	/.class
		 * suggested fix:
		 * observed:	//////////////.class
		 * answer:		//////////////.class
		 * answer fix:    .replaceAll("\\.","/")
		 * (first arg is a regex, matches "any" and thus replaces everything. due to "all". don't need the *
		 */
	}
}
