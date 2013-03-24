/*
 * Cory Savit
 * cas241@pitt.edu
 * CS1541
 * Project2: ClothCutter
 */
public class Pattern {
	public int height;
	public int width;
	public int value;
	public String name;

	//blank Pattern constructor
	public Pattern() {
		this.height = 0;
		this.width = 0;
		this.value = 0;
		this.name = "";
	}
	
	//pattern constructor for pass from TestClothCutter
	public Pattern(int w, int h, int v, String n){
		this.height = h;
		this.width = w;
		this.value = v;
		this.name = n;
	}
	
	//pattern fit method for optimize. returns true for a fitm and false if not.
	public boolean fit(int w, int h) {
		if(this.height <= h && this.width <= w) return true;
		return false;
	}
	
}

