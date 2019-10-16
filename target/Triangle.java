package target;
import java.util.HashSet;
import java.util.Set;
public class Triangle {
  private double side1;
  private double side2;
  private double side3;
  public Triangle(  double side1,  double side2,  double side3) throws TriangleException {
    CoverageTransformer.getInstance().logIn("loggin").info("*16*");
    this.side1=side1;
    this.side2=side2;
    this.side3=side3;
    if (allSidesAreZero() || hasImpossibleSides() || violatesTriangleInequality()) {
      throw new TriangleException();
      CoverageTransformer.getInstance().logIn("loggin").info("*22*");
    }
  }
  public TriangleKind getKind(){
    CoverageTransformer.getInstance().logIn("loggin").info("*28*");
    int uniqueSides=getNumberOfUniqueSides();
    CoverageTransformer.getInstance().logIn("loggin").info("*30*");
    if (uniqueSides == 1) {
      return TriangleKind.EQUILATERAL;
      CoverageTransformer.getInstance().logIn("loggin").info("*32*");
    }
    if (uniqueSides == 2) {
      return TriangleKind.ISOSCELES;
      CoverageTransformer.getInstance().logIn("loggin").info("*36*");
    }
    return TriangleKind.SCALENE;
  }
  private boolean allSidesAreZero(){
    return side1 == 0 && side2 == 0 && side3 == 0;
    CoverageTransformer.getInstance().logIn("loggin").info("*44*");
  }
  private boolean hasImpossibleSides(){
    return side1 < 0 || side2 < 0 || side3 < 0;
    CoverageTransformer.getInstance().logIn("loggin").info("*48*");
  }
  private boolean violatesTriangleInequality(){
    return side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1;
    CoverageTransformer.getInstance().logIn("loggin").info("*52*");
  }
  public int getNumberOfUniqueSides(){
    Set<Double> sides=new HashSet<>();
    CoverageTransformer.getInstance().logIn("loggin").info("*57*");
    sides.add(side1);
    sides.add(side2);
    CoverageTransformer.getInstance().logIn("loggin").info("*60*");
    sides.add(side3);
    CoverageTransformer.getInstance().logIn("loggin").info("*61*");
    CoverageTransformer.getInstance().logIn("loggin").info("*62*");
    return sides.size();
  }
}
