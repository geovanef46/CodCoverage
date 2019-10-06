package codcoverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

    
    public class ASTnodeFinder extends ASTVisitor {
        private final List <ASTNode> astnodes = new ArrayList<>();
        

        /**Percorre a AST retornando os ASTnodes
         * @param node
         * @return
         */
        public static List<ASTNode> getASTnodes(ASTNode node) {
            ASTnodeFinder finder = new ASTnodeFinder();
            node.accept(finder);
            return finder.getASTnode();
        }
        
        
        @Override
        public boolean visit (final MethodDeclaration node) {
          astnodes.add (node);
          return super.visit(node);
           
        }
        
        @Override
        public boolean visit (final PackageDeclaration node) {
          astnodes.add (node);
          return super.visit(node);
           
        }
        
        @Override
        public boolean visit (final ImportDeclaration node) {
          astnodes.add (node);
          return super.visit(node);
           
        }
        
        @Override
        public boolean visit (final MethodInvocation node) {
          astnodes.add (node);
          return super.visit(node);
           
        }
        
        
        
        

     
        public List <ASTNode> getASTnode() {
          return Collections.unmodifiableList(astnodes);
        }

    }
    
 