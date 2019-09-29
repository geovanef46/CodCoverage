package codcoverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

    
    public class MethodDeclarationFinder extends ASTVisitor {
        private final List <MethodDeclaration> methods = new ArrayList<>();
        private final List <SimpleName> simpleNames = new ArrayList<>();

        /**Percorre a AST retornando os métodos encontrados 
         * @param node
         * @return
         */
        public static List<MethodDeclaration> getMethDeclarations(ASTNode node) {
            MethodDeclarationFinder finder = new MethodDeclarationFinder();
            node.accept(finder);
            return finder.getMethods();
        }
        
        public static List<SimpleName> getNames(ASTNode node) {
            MethodDeclarationFinder finder = new MethodDeclarationFinder();
            node.accept(finder);
            return finder.getSimpleName();
        }
        
        @Override
        public boolean visit (final MethodDeclaration method) {
          methods.add (method);
          return super.visit(method);
        }
        
        @Override
        public boolean visit (final SimpleName methodInv) {
          simpleNames.add (methodInv);
          return super.visit(methodInv);
        }

     
        public List <MethodDeclaration> getMethods() {
          return Collections.unmodifiableList(methods);
        }
        
        public List <SimpleName> getSimpleName() {
            return Collections.unmodifiableList(simpleNames);
          }
    }
    
 