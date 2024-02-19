package tests;

import syntaxtree.*;
import visitor.*;
public class Practice
{
    public static void main(String[] args)
    {
        VarDeclList v2 = new VarDeclList();
        StatementList v4 = new StatementList();
        v4.add(new Assign(111,new IdentifierExp(109,"x"),new Plus(115,new IdentifierExp(113,"x"),new IntegerLiteral(117,1))));
        ExpList v8 = new ExpList();
        v8.add(new IdentifierExp(201,"i"));
        StatementList v7 = new StatementList();
        v7.add(new CallStatement(195,new Call(195,new IdentifierExp(188,"System"),"print",v8)));
        StatementList v6 = new StatementList();
        v6.add(new Block(174,v7));
        v6.add(new Assign(161,new IdentifierExp(161,"i"),new Plus(162,new IdentifierExp(161,"i"),new IntegerLiteral(162,1))));
        StatementList v5 = new StatementList();
        v5.add(new LocalDeclStatement(146,new LocalVarDecl(146,new IntegerType(142),"i",new IntegerLiteral(150,0))));
        v5.add(new While(138,new LessThan(155,new IdentifierExp(153,"i"),new IntegerLiteral(157,10)),new Block(138,v6)));
        StatementList v3 = new StatementList();
        v3.add(new LocalDeclStatement(65,new LocalVarDecl(65,new IntegerType(61),"x",new IntegerLiteral(69,0))));
        v3.add(new While(80,new Not(88,new GreaterThan(88,new IdentifierExp(86,"x"),new IntegerLiteral(91,10))),new Block(95,v4)));
        v3.add(new Block(138,v5));
        VarDeclList v9 = new VarDeclList();
        v9.add(new FormalDecl(244,new IntegerType(240),"n"));
        StatementList v11 = new StatementList();
        v11.add(new Assign(306,new IdentifierExp(302,"ret"),new IntegerLiteral(308,1)));
        ExpList v13 = new ExpList();
        v13.add(new Minus(351,new IdentifierExp(350,"n"),new IntegerLiteral(352,1)));
        ExpList v14 = new ExpList();
        v14.add(new Minus(362,new IdentifierExp(361,"n"),new IntegerLiteral(363,2)));
        StatementList v12 = new StatementList();
        v12.add(new Assign(344,new IdentifierExp(340,"ret"),new Plus(355,new Call(346,new This(346),"fib",v13),new Call(357,new This(357),"fib",v14))));
        StatementList v10 = new StatementList();
        v10.add(new LocalDeclStatement(261,new LocalVarDecl(261,new IntegerType(257),"ret",new IntegerLiteral(267,0))));
        v10.add(new If(278,new LessThan(283,new IdentifierExp(281,"n"),new IntegerLiteral(285,2)),new Block(288,v11),new Block(326,v12)));
        DeclList v1 = new DeclList();
        v1.add(new MethodDeclVoid(44,"main",v2,v3));
        v1.add(new MethodDeclNonVoid(236,new IntegerType(232),"fib",v9,v10,new IdentifierExp(392,"ret")));
        ClassDeclList v0 = new ClassDeclList();
        v0.add(new ClassDecl(6,"Main","Object",v1));
        AstNode ast = new Program(0,v0);
        PrettyPrintVisitor pp = new PrettyPrintVisitor(System.out);
        ast.accept(pp);
    }
}

