package codcoverage.source.src.example;
CoverageTransformer.logIn("Triangle.java").info("*1*");


import java.util.HashSet;
import java.util.Set;


 
public class Triangle { 
   

    private double side1; 
    private double side2; 
    private double side3; 
 
    public Triangle(double side1, double side2, double side3) throws TriangleException { 
CoverageTransformer.logIn("Triangle.java").info("*16*");

        this.side1 = side1; 
        this.side2 = side2; 
        this.side3 = side3; 
 
        if (allSidesAreZero() || hasImpossibleSides() || violatesTriangleInequality()) { 
CoverageTransformer.logIn("Triangle.java").info("*22*");
            throw new TriangleException(); 
        }

    } 
 
    public TriangleKind getKind() { 
CoverageTransformer.logIn("Triangle.java").info("*28*");

        int uniqueSides = getNumberOfUniqueSides(); 
CoverageTransformer.logIn("Triangle.java").info("*30*");
 
        if (uniqueSides == 1) { 
CoverageTransformer.logIn("Triangle.java").info("*32*");
            return TriangleKind.EQUILATERAL; 
        } 
 
        if (uniqueSides == 2) { 
CoverageTransformer.logIn("Triangle.java").info("*36*");
            return TriangleKind.ISOSCELES; 
        } 
 
        return TriangleKind.SCALENE; 
        
    } 
 
    private boolean allSidesAreZero() { 
CoverageTransformer.logIn("Triangle.java").info("*44*");
        return side1 == 0 && side2 == 0 && side3 == 0; 
    } 
 
    private boolean hasImpossibleSides() { 
CoverageTransformer.logIn("Triangle.java").info("*48*");
        return side1 < 0 || side2 < 0 || side3 < 0; 
    } 
 
    private boolean violatesTriangleInequality() { 
CoverageTransformer.logIn("Triangle.java").info("*52*");
        return side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1; 
    } 
 
 
    public int getNumberOfUniqueSides() { 
CoverageTransformer.logIn("Triangle.java").info("*57*");
        Set<Double> sides = new HashSet<>(); 
 
        sides.add(side1); 
CoverageTransformer.logIn("Triangle.java").info("*60*");
        sides.add(side2); 
CoverageTransformer.logIn("Triangle.java").info("*61*");
        sides.add(side3); 
CoverageTransformer.logIn("Triangle.java").info("*62*");
 
        return sides.size(); 
CoverageTransformer.logIn("Triangle.java").info("*64*");
    } 
}
