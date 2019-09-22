/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codcoverage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;



/**
 * Deve receber o source e reescrever em target
 * @author
 * 
 **/
public class CoverageTransformer {
    String path = "src/codcoverage/source/src/example/Triangle.java";
    String activityPath; 
    


public String insertFile() {
        String source="";
        try {
         source = FileUtils.readFileToString(new File(path), "UTF-8");
         System.out.println("Arquivo obtido!");
        } catch (Exception e) {
            System.out.println("Deu ruim... não encontrou o arquivo");
        }

      return source;      
 }

 
 public boolean insertLog() throws SecurityException{ 
     Map options = JavaCore.getOptions();
     JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
     
     Logger log = logIn();
     ASTParser parser = ASTParser.newParser(AST.JLS8);
     parser.setSource(insertFile().toCharArray());
     parser.setCompilerOptions(options);
     parser.setKind(ASTParser.K_CLASS_BODY_DECLARATIONS);

 CompilationUnit unit = (CompilationUnit) parser.createAST(null);
 AST ast = unit.getAST();

 List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(unit);
 for (MethodDeclaration methodDeclaration : methodDeclarations) {
     MethodInvocation methodInvocation = ast.newMethodInvocation();
     
     methodInvocation.setName(ast.newSimpleName("logIn"));
     
     methodInvocation.setName(ast.newSimpleName("log"));
     
     StringLiteral literal = ast.newStringLiteral();
     literal.setLiteralValue("Level.INFO,\"passou no\""+methodDeclaration.getName().getFullyQualifiedName());
     methodInvocation.arguments().add(literal);
     
     System.out.println(methodDeclaration.getName().getFullyQualifiedName());
//     
//
//     // System.out.println("Hello, World")
//     
//     methodInvocation.setExpression(qName);
//     methodInvocation.setName(ast.newSimpleName("println"));
//
//     StringLiteral literal = ast.newStringLiteral();
//     literal.setLiteralValue("Hello, World");
//     methodInvocation.arguments().add(literal);
//
//     // Append the statement
//     methodDeclaration.getBody().statements().add(ast.newExpressionStatement(methodInvocation));
 }
 return true;
 }
 
 
 public Logger logIn() {
     
     Logger log = Logger.getLogger("Log");
     FileHandler filetxt = null;

         try {
            filetxt = new FileHandler("loggin.txt");
        } catch (SecurityException | IOException e) {
            // TODO Auto-generated catch block
           
        }
         filetxt.setFormatter(new SimpleFormatter());

     log.addHandler(filetxt);
     
     return log;
    
}
 
 
 
 
 public boolean directoryNav() {
     
     return true;
 }
 
 
 public static void main(String[] args) {
     
     CoverageTransformer cc = new CoverageTransformer();
     try {
         cc.insertLog();
    } catch (SecurityException e) {
       System.out.println("Erro de assinatura da biblioteca..."+ e.getMessage());
    }
     
     
 }




    
}
