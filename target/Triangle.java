package codcoverage.source.src.example;
codcoverage.CoverageTransformer.logIn().info("*1*");


import java.util.HashSet;
import java.util.Set;


 
public class Triangle { 
   

    private double side1; 
    private double side2; 
    private double side3; 
 
    public Triangle(double side1, double side2, double side3) throws TriangleException { 
codcoverage.CoverageTransformer.logIn().info("*16*");

        this.side1 = side1; 
        this.side2 = side2; 
        this.side3 = side3; 
 
        if (allSidesAreZero() || hasImpossibleSides() || violatesTriangleInequality()) { 
codcoverage.CoverageTransformer.logIn().info("*22*");
            throw new TriangleException(); 
        }

    } 
 
    public TriangleKind getKind() { 
codcoverage.CoverageTransformer.logIn().info("*28*");

        int uniqueSides = getNumberOfUniqueSides(); 
codcoverage.CoverageTransformer.logIn().info("*30*");
 
        if (uniqueSides == 1) { 
codcoverage.CoverageTransformer.logIn().info("*32*");
            return TriangleKind.EQUILATERAL; 
codcoverage.CoverageTransformer.logIn().info("*33*");
        } 
 
        if (uniqueSides == 2) { 
codcoverage.CoverageTransformer.logIn().info("*36*");
            return TriangleKind.ISOSCELES; 
codcoverage.CoverageTransformer.logIn().info("*37*");
        } 
 
        return TriangleKind.SCALENE; 
codcoverage.CoverageTransformer.logIn().info("*40*");
        
    } 
 
    private boolean allSidesAreZero() { 
codcoverage.CoverageTransformer.logIn().info("*44*");
        return side1 == 0 && side2 == 0 && side3 == 0; 
codcoverage.CoverageTransformer.logIn().info("*45*");
    } 
 
    private boolean hasImpossibleSides() { 
codcoverage.CoverageTransformer.logIn().info("*48*");
        return side1 < 0 || side2 < 0 || side3 < 0; 
codcoverage.CoverageTransformer.logIn().info("*49*");
    } 
 
    private boolean violatesTriangleInequality() { 
codcoverage.CoverageTransformer.logIn().info("*52*");
        return side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1; 
codcoverage.CoverageTransformer.logIn().info("*53*");
    } 
 
 
    public int getNumberOfUniqueSides() { 
codcoverage.CoverageTransformer.logIn().info("*57*");
        Set<Double> sides = new HashSet<>(); 
 
        sides.add(side1); 
codcoverage.CoverageTransformer.logIn().info("*60*");
        sides.add(side2); 
codcoverage.CoverageTransformer.logIn().info("*61*");
        sides.add(side3); 
codcoverage.CoverageTransformer.logIn().info("*62*");
 
        return sides.size(); 
codcoverage.CoverageTransformer.logIn().info("*64*");
    } 
}
