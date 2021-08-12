import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BigIntTest {
   public static void main(String[] args) throws FileNotFoundException {
      Scanner stdin = new Scanner(new File("C:\\Users\\grc91694\\eclipse-workspace\\BigInt\\src\\text.txt"));

      while (stdin.hasNext()) {
         String aNum = stdin.next();
         String op = stdin.next();
         String bNum = stdin.next();

         BigInt a = new BigInt(aNum);
         BigInt b = new BigInt(bNum);

         System.out.println("a = " + a);
         System.out.println("b = " + b);

         if (op.equals("+")) {

            BigInt c = a.add(b);
            System.out.println("a + b = " + c);
         } else if (op.equals("-")) {

            BigInt c = a.subtract(b);
            System.out.println("a - b = " + c);
         } else if (op.equals("*")) {
            BigInt c = a.multiply(b);
            System.out.println("a * b = " + c);
         } else if (op.equals("/")) {
            BigInt c = a.divide(b);
            System.out.println("a / b = " + c);
         }
      }
      
   }
}