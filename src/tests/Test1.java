package tests;

import syntaxtree.*;
import visitor.*;

public class Test1
{
    public static void main(String[] args)
    {
        VarDeclList v2 = new VarDeclList();
        StatementList v3 = new StatementList();
        v3.add(new LocalDeclStatement(61,new LocalVarDecl(61,new IntegerType(57),"y",new IntegerLiteral(65,4))));
        v3.add(new LocalDeclStatement(74,new LocalVarDecl(74,new IntegerType(70),"z",new Plus(80,new IdentifierExp(78,"x"),new IdentifierExp(82,"y")))));
        VarDeclList v4 = new VarDeclList();
        v4.add(new FormalDecl(108,new IntegerType(104),"z"));
        v4.add(new FormalDecl(115,new IntegerType(111),"b"));
        StatementList v8 = new StatementList();
        v8.add(new Assign(159,new IdentifierExp(157,"z"),new Plus(163,new IdentifierExp(161,"z"),new IdentifierExp(165,"i"))));
        StatementList v7 = new StatementList();
        v7.add(new Block(152,v8));
        v7.add(new Assign(147,new IdentifierExp(147,"i"),new Plus(148,new IdentifierExp(147,"i"),new IntegerLiteral(148,1))));
        StatementList v6 = new StatementList();
        v6.add(new LocalDeclStatement(131,new LocalVarDecl(131,new IntegerType(127),"i",new IntegerLiteral(135,0))));
        v6.add(new While(122,new LessThan(140,new IdentifierExp(138,"i"),new IntegerLiteral(142,100)),new Block(122,v7)));
        StatementList v5 = new StatementList();
        v5.add(new Block(122,v6));
        DeclList v1 = new DeclList();
        v1.add(new InstVarDecl(30,new IntegerType(26),"x"));
        v1.add(new MethodDeclVoid(46,"main",v2,v3));
        v1.add(new MethodDeclNonVoid(100,new IntegerType(96),"add",v4,v5,new Plus(183,new IdentifierExp(181,"z"),new IdentifierExp(185,"b"))));
        ClassDeclList v0 = new ClassDeclList();
        v0.add(new ClassDecl(6,"Main","Lib",v1));
        AstNode ast = new Program(0,v0);
        PrettyPrintVisitor pp = new PrettyPrintVisitor(System.out);
        ast.accept(pp);
    }
}

