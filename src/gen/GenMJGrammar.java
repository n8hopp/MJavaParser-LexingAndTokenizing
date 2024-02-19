package gen;

public class GenMJGrammar
{

    public static void main(String[] args)
    {

        try
        {
            wrangLR.generator.main.Main.main(new String[]
                                             {
                                                 "MJGrammar.java",
//					"-n",
                                                 "-d", "src/parse",
                                                 "-o", "MJGrammarParseTable.java",
                                             });
        }
        catch (Exception x)
        {
            System.out.println("Exception occurred.");
        }

    }
}
