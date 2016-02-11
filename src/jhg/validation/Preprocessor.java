package jhg.validation;

public abstract class Preprocessor {

	protected PreprocessResult report;
	public Preprocessor() {
		super();
		this.report = new PreprocessResult();
	}

	public PreprocessResult getReport() {
		return this.report;
	}

	public abstract void execute();
	
}
