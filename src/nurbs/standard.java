package nurbs;
public class standard{
	public static void main(String[] args){
		StandardFlaeche test = new StandardFlaeche(20);
		for (int i = 0; i < test.getNetz().length; i++){
			for (int j = 0; j < test.getNetz().length; j++){
				System.out.print(test.getNetz()[j][i].getX() + " ");
			}
			System.out.println();
		}
	}
}