package jhg.data.analysis;

/**
 * Mutable with limited incrementing application.
 * 
 * @author jgagon
 *
 */
public class IncrementingInt implements Comparable<IncrementingInt>{
	private Integer value = 0;
	public IncrementingInt(){
		super();
	}
	public IncrementingInt(IncrementingInt ii){
		this.value = ii.value;
	}
	public void increment(){ 
		++value;
	}
	public Integer get(){
		return this.value;
	}
	@Override
	public int compareTo(IncrementingInt arg0) {
		return value.compareTo(arg0.get());
	}

	public String toString(){
		return value.toString();
	}
}
