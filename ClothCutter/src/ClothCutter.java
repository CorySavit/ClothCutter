/*
 * Cory Savit
 * cas241@pitt.edu
 * CS1541
 * Project2: ClothCutter
 */
import java.util.ArrayList;

public class ClothCutter {
	public boolean type;
	public int width;
	public int height;
	public int value;
	public int cut;
	public Pattern pattern;
	public Pattern nullPattern;
	public ClothCutter cloth1;
	public ClothCutter cloth2;
	public ClothCutter nul;
	private ArrayList<Pattern> patterns = new ArrayList<Pattern>();
	private ArrayList<Cut> cuts = new ArrayList<Cut>();
	private ArrayList<Garment> garments = new ArrayList<Garment>();

	private int bestTemp1;
	private int bestTemp2;
	
	//ClothCutter constructor without passed parameters. Creates a, generally, nulled ClothCutter object.
	public ClothCutter(){
		this.type = false;
		this.width = 0;
		this.height = 0;
		this.cut = 0;
		this.value = 0;
		this.pattern = nullPattern;
		this.cloth1 = nul;
		this.cloth2 = nul;
	}
	
	/*Clothcutter Constructor. with 3 arguments. Really only used for the first pass from
	 * optimize() to optimize(int w, int h, ClothCutter[][] memo)
	 */
	public ClothCutter(int w, int h, ArrayList<Pattern> p){
		this.width = w;
		this.height = h;
		this.patterns = p;	
		this.cloth1= nul;
		this.cloth2= nul;
	}
	
	//ClothCutter constructor for most ClothCutter objects
	public ClothCutter(boolean type, int width, int height,int cut,int value, Pattern pattern, ClothCutter cloth1, ClothCutter cloth2){
		this.type = type;
		this.width = width;
		this.height = height;
		this.cut = cut;
		this.value = value;
		this.pattern = pattern;
		this.cloth1 = cloth1;
		this.cloth2 = cloth2;
	}

	//redirected optimize, and calls to get value, cuts, and garments from the completed optimized matrix
	public void optimize () {
		//memo is a matrix of opimized cuts.
		ClothCutter[][] memo = new ClothCutter[this.width+1][this.height+1];
		//call other optimize method with heigth and width of original ClothCutter, as well as the memo matrix
		optimize(this.width, this.height, memo);
		//set value of top element to this.value
		this.value = memo[this.width][this.height].value;
		//get cuts starts at the top element and traverses the matrix as a tree, collecting cuts
		getCuts(0,0, memo[this.width][this.height], this.cuts);
		//getGarments behaves similarly to getCuts
		getGarments(0,0, memo[this.width][this.height], this.garments);
	}
	
	//optimize funtion. It is passed previously optimized cloths to help with recursion
	public void optimize (int w, int h, ClothCutter[][] memo) {
		int best = 0;
		boolean type = false;	//boolean variable used to determine horizontal or vertical cuts later.

		//test case against memoize matrix, if the opimized value is stored, do not optimize
		if(memo[w][h]!=null){
			return;
		}
		
		//optimized for loop for all elements in this.patterns
		//fit highest value pattern into the uncut fabric piece
		ClothCutter temp = new ClothCutter(false, w, h, 0, 0, null, null, null);
		for (Pattern p: this.patterns){
			if(p.fit(w, h) && p.value > best){
				temp = new ClothCutter(false, w, h, 0, p.value, p, null, null);
				best = p.value;
			}
				//if not pattern fits, return.
				if(best==0) return;
		}
		
		Pattern none = new Pattern();	//filler pattern for parent nodes, since they have no pattern
		
		//iterate through cuts along width(vertical) and recursively optimize for all cuts
		if (best !=0){				//maybe redundant, added earlier in program writing. Don't optimize if no pattern fits.
		for (int i=1; i<w; i++) {
			ClothCutter left = new ClothCutter(i, h, this.patterns);
			ClothCutter right = new ClothCutter(w-i, h, this.patterns);
			//check this size cloth against previously optimized cloths, and then optimize if necissary
			optimize(i, h, memo);
			optimize(w-i, h, memo);
			left = (ClothCutter)memo[i][h];				//set left and right to memoized ClothCutter
			right = (ClothCutter)memo[w-i][h];
			
			//check to make sure that left and right have children. Again, possible redundant.
			if (left!=null && right!=null){
			//add the values of both optimized sides
			bestTemp1 = left.value + right.value; 
				//if the added values are greater than the uncut best value, or previous cut values add info to best cut
				if(bestTemp1 > best){
					type = false;
					best = bestTemp1;
					temp = new ClothCutter(type, w, h, i, best, none, left, right); 
				}
			}
		}
		
		//iterate through cuts along height(horizontal) and recursively optimize
		for (int i=1; i<h; i++){
			
			ClothCutter top = new ClothCutter(w, i, this.patterns);
			ClothCutter bottom = new ClothCutter(w, h-i, this.patterns);
			//check this size cloth against previously optimized cloths
			optimize(w, i, memo);
			optimize(w, h-i, memo);
			top = (ClothCutter)memo[w][i];
			bottom = (ClothCutter)memo[w][h-i];
			if(top!=null && bottom!=null){
				bestTemp2= top.value + bottom.value;
				//add the values of both optimized sides
				//if the added values are greater than the uncut best value, or previous cut values add info to best cut
				if(bestTemp2 > best){
					type = true;
					best = bestTemp2;
					temp = new ClothCutter(type, w, h, i, best, none, top, bottom);
				}
			}
		}

		memo[w][h]=temp;		//save the best cut's ClothCutter in the memoization matrix
	}
}
		
		
		

	


	
	//recursively find cuts.
	//first time through it uses coordinates 0,0 then it passed the previous cuts width and height
	public void getCuts(int x, int y, ClothCutter c, ArrayList<Cut> j){
		//maybe redundant, make sure the cloth to be cut, actually should be cut
		if(c.cloth1==null) return;
			j.add(new Cut(x, y, c));
			getCuts(x, y, c.cloth1, j);
			if(!c.type) getCuts(x+c.cloth1.width, y, c.cloth2, j);
			else if (c.type) getCuts(x, y + c.cloth1.height, c.cloth2, j);
			return;
		
	}
	
	public void getGarments(int x, int y, ClothCutter c, ArrayList<Garment> j){
		//if the passed ClothCutter(cut fabric) has a first child(subcut) recursively get its garments
		if (c.cloth1 != null) {
			getGarments(x,y,c.cloth1, j);
			//depending on cut type, appropriately change x or y value to move to the next ClothCutter
			if (!c.type) x += c.cloth1.width;
			if (c.type) y += c.cloth1.height;
			getGarments(x,y,c.cloth2,j);
		}
		//If this is an optimal cut, with a pattern, add the garment and pattern to the array
		if (c.pattern.value != '0') j.add(new Garment(x,y,c.width,c.height,c.pattern.value,c.pattern.name));
		
	}
		 
	//return appropriate values. used by optimize() and calls in TestClothCutter
	public int value() {
	    return this.value;
} 

	public ArrayList<Cut> cuts() {
		   return this.cuts; 
	} 
	
	public ArrayList<Garment> garments() {
	    return this.garments;
} 

}
