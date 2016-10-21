package jhg.decomposition;

public class Main {

	
	@SuppressWarnings("unused")
	public static void main(String[] args){
		
		String txta = "";//JSON format
		String txtb = "";//JSON format
				
		DataSet a2015 = new DataSet(txta);
		DataSet b2016 = new DataSet(txtb);
		
		DecompositionAnalyzer analyzer = new DecompositionAnalyzer();
		/*
		 * 
		 * DataSet looks like this. an identity label. at least 3 variables. Let's say obesity(pounds over bmi), blood pressure (mmHG over), longevity(average years e.g: 71-85)
		 * 
		 *    O   B   L
		 * AL 15 15 71
		 * AK 20 5  79
		 * AZ 10 15 73
		 * AR 5  10 75
		 * 
		 * Then we have a change set.
		 * 
		 *    O   B   L
		 * AL 22 17 70
		 * AK 21 5  75
		 * AZ 11 16 72
		 * AR 4  11 71
		 * 
		 * First we get the deltas. Then we can rotate through variables in terms of being factors and outcomes
		 *  
		 * Independent/explanatory factor - Dependent/Response variable
		 * O-L
		 * B-L
		 * O-B
		 * B-O
		 * L-O
		 * L-B
		 * 
		 * so let's say Obesity change somewhere was 5% and Longevity went down 10% (comparing percentage change eliminates scale issues). 
		 * There is an apparent inverse correlation of obesity on longevity with one point. Longevity reduction could be compounded by obesity
		 * or it could be other factors. Is the correlation consistent? If it varies a lot, then maybe not. 
		 * We can then identify which variables are better linked than others. 
		 * 
		 * 
		 * known causation.
		 * 
		 * 
		 * LAW OF TOTAL VARIANCE 
		 * In order to understand the decomposition of variance, it is necessary to understand the law of total variance. Assume 
		 * that there are two variables; Y = dependent variable or response variable, and X = independent variable or explanatory 
		 * factor. In the general linear model, the relationship is capture by the linear equation: (1) Y = a + bX + c
		 * 
Simply state, for every change of X, there is a corresponding change in Y. The focus of variance decomposition is on the response variable: Y. Specifically, the variance of Y, which is given by:

(2 Var(Y) = E(Var[Y|X]) + Var(E[Y|X])

In the relationship between X and Y, the variance of Y (dependent variable) is comprised of (i) the expected variance of Y with respect to X, plus (ii) the variance of the “expected variance of Y” with respect to X. In simple language, the variance of Y is its expected value plus the “variance of this expected value.” This is sometimes summarized as:

E(Var[Y|X]) = explained variation directly due to changes in X
Var(E[Y|X]) = unexplained variation comes from somewhere other than X

DECOMPOSITION
The decomposition of variance is used when we are dealing with dynamic stochastic system. Stochastic system is a random value process. This stochastic system may be defined as:
Y(t) = value of system at time (t)
H(it) = historical value corresponding to (t) where H)it) = H(1t), H(2t), …, H(c-1, t)

From equation (3), the statement may be rewritten in terms of Y(t) = Y and H(it) = X as:

(3) Var[Y(t)] = E(Var[Y(t) | H(1t), H(2t, …, H(c-1,t)]) + Sum(E[Var(Y(t)] | H(1t), H(2t, …, H(j-1,t)]) + Var(E[Y(t)] | H(1t))

MEANING OF THE RESULT
Recall these two conditions:
E(Var[Y|X]) = explained variation directly due to changes in X; and
Var(E[Y|X]) = unexplained variation comes from somewhere other than X.
The result helps the researcher to isolate to appreciate the fact that the response in Y has variation; this variation is comprised of 2 components. When these components are decomposed they are one type of variation that is explained by the changes of X (independent variable) and another variance that is completely due to chance stance, i.e. unexplained. Another meaning of this is that Var(E[Y | X]) = randomness; after all, randomness is defined as unpredictable pattern.
		 * 

Suppose that two factories supply light bulbs to the market. Factory X's bulbs work for an average of 5000 hours, whereas factory Y's bulbs work for an average of 4000 hours. It is known that factory X supplies 60% of the total bulbs available. What is the expected length of time that a purchased bulb will work for?

Applying the law of total expectation, we have:

5000(.6) + 4000(.4) = 4600
3000       1600

If a total expectation is 4600 and X is 0.6 and Y is 0.4 then, then in the reverse:




		 */
		
	}
	
}
