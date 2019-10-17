package coverage.target;
import java.util.HashSet;
import java.util.Set;
import codcoverage.CoverageTransformer;
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
      CoverageTransformer.getInstance().logIn("loggin").info("*22*");
      throw new TriangleException();
    }
  }
  public TriangleKind getKind(){
    CoverageTransformer.getInstance().logIn("loggin").info("*28*");
    int uniqueSides=getNumberOfUniqueSides();
    CoverageTransformer.getInstance().logIn("loggin").info("*30*");
    if (uniqueSides == 1) {
      CoverageTransformer.getInstance().logIn("loggin").info("*32*");
      return TriangleKind.EQUILATERAL;
    }
    if (uniqueSides == 2) {
      CoverageTransformer.getInstance().logIn("loggin").info("*36*");
      return TriangleKind.ISOSCELES;
    }
    return TriangleKind.SCALENE;
  }
  private boolean allSidesAreZero(){
    CoverageTransformer.getInstance().logIn("loggin").info("*44*");
    return side1 == 0 && side2 == 0 && side3 == 0;
  }
  private boolean hasImpossibleSides(){
    CoverageTransformer.getInstance().logIn("loggin").info("*48*");
    return side1 < 0 || side2 < 0 || side3 < 0;
  }
  private boolean violatesTriangleInequality(){
    CoverageTransformer.getInstance().logIn("loggin").info("*52*");
    return side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1;
  }
  public int getNumberOfUniqueSides(){
    CoverageTransformer.getInstance().logIn("loggin").info("*57*");
    Set<Double> sides=new HashSet<>();
    sides.add(side1);
    CoverageTransformer.getInstance().logIn("loggin").info("*60*");
    sides.add(side2);
    CoverageTransformer.getInstance().logIn("loggin").info("*61*");
    sides.add(side3);
    CoverageTransformer.getInstance().logIn("loggin").info("*62*");
    CoverageTransformer.getInstance().logIn("loggin").info("*64*");
    return sides.size();
  }
}
