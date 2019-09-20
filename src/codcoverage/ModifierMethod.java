package codcoverage;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class ModifierMethod {
    
    
           public String insertFile() {
               String source="";
               try {
                source = FileUtils.readFileToString(new File("src/source/example/Triangle.java"), "UTF-8");
               } catch (Exception e) {
                   System.out.println("Deu ruim... não encontrou o arquivo");
               }

             return source;      
        }

        
        public boolean insertHello() { 
            
            ASTParser parser = ASTParser.newParser(AST.JLS8);
            parser.setSource(insertFile().toCharArray());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
        AST ast = unit.getAST();

        List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(unit);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            MethodInvocation methodInvocation = ast.newMethodInvocation();

            // System.out.println("Hello, World")
            QualifiedName qName = ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
            methodInvocation.setExpression(qName);
            methodInvocation.setName(ast.newSimpleName("println"));

            StringLiteral literal = ast.newStringLiteral();
            literal.setLiteralValue("Hello, World");
            methodInvocation.arguments().add(literal);

            // Append the statement
            methodDeclaration.getBody().statements().add(ast.newExpressionStatement(methodInvocation));
        }
        return true;
        }
        
        public static void main(String[] args) {
            
            ModifierMethod MM = new ModifierMethod();
            MM.insertHello();
            
        }
   }
