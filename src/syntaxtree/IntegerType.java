package syntaxtree;
import java.io.PrintStream;

import visitor.Visitor;

/**
 * the type 'int'
 */
public class IntegerType extends Type
{

    /**
     * constructor
     * @param pos file position
     */
    public IntegerType(int pos)
    {
        super(pos);
    }

    /**
     * type equality
     * @param the object tested for being equal to me
     */
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof IntegerType;
    }

    /**
     * hash code
     * @return the object's hash code
     */
    @Override
    public int hashCode()
    {
        return 657643445;
    }

    /*** remaining methods are visitor- and display-related ***/

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
    public String toString2()
    {
        return "int";
    }

}
