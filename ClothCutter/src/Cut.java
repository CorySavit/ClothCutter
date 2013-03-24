/*
 * Cory Savit
 * cas241@pitt.edu
 * CS1541
 * Project2: ClothCutter
 */
public class Cut {
	public boolean type;
	public int width;
	public int height;
	public int cut;
	public int value;
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public ClothCutter cloth1;
	public ClothCutter cloth2;
	
	
	
	public Cut(int x, int y, ClothCutter c){
		//if vertical cut
		if (!c.type){
			this.x1 = x + c.cloth1.width;
			this.y1 = y;
			this.x2 = x + c.cloth1.width;
			this.y2 = y + c.height;
		}
		//if horizontal cut
		if (c.type){
			this.x1 = x;
			this.y1 = y + c.cloth1.height;
			this.x2 = x + c.width;
			this.y2 = y + c.cloth1.height;
		}
		
	}
}


