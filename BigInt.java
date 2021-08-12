
public class BigInt {
   public int[] value;
   public int sign;
   public int change = 0;
   //each digit in the array can have a maximum of 4 digits
   public static final int MODCONSTANT = 10000;

   //empty contructor
   public BigInt() {

   }


   //construct a BigInt object from a string, can't use int
   public BigInt(String digits) {
      //determines where you start in the string
      int beginIndex = 0;
      
      if (digits.charAt(0) == '-') {
         //assign negative attribute
         this.sign = -1;
         //don't include the negative sign in the array
         //start at one place to the right
         beginIndex++;
      } else {
         this.sign = 1;
      }
      //if it is not divisible by 4, you will have an extra space for the remainder
      if ((digits.length()-beginIndex) % 4 != 0) {
         value = new int[(digits.length()-beginIndex) / 4 + 1];
         //if it is, you don't need an extra space for remainder
      } else {
         value = new int[(digits.length()-beginIndex) / 4];
      }

      //use x to count the spaces in the array, because you skip by 4 in the string
      int x = 0;
      //begin at either 0 or 1 depending on sign
      //insert 4 digits at a time
      for (int i = beginIndex; i < digits.length(); i = i + 4) {

         //if there are 4 or more digits left
         if (digits.length() - i >= 4) {
            //insert the 4 numbers prior to the last number you were at
            //digits.length()-i-4 starts at the end-4, and after each increment, moves 4 to the left
            //digits.length()-i starts at the end, and moves 4 to the left
            value[x] = (int) (Long.parseLong(digits.substring(digits.length() - i - 4 + beginIndex, digits.length() - i + beginIndex)));

            //if there are less than 4, you can start at 0 because its the last space in the array
            //for the other elses, if there are less than 3 digits, insert into one array space
         } else if (digits.length() - i == 3) {
            value[x] = (int) (Long.parseLong(digits.substring(beginIndex, beginIndex+3)));

         } else if (digits.length() - i == 2) {
            value[x] = (int) (Long.parseLong(digits.substring(beginIndex, beginIndex+2)));

         } else {
            value[x] = (int) (Long.parseLong(digits.substring(beginIndex, beginIndex+1)));
         }
         //go to the next space in the array
         x++;
      }
   }

   public BigInt add(BigInt another) {
      BigInt sum = new BigInt();

      //if they are both positive or negative
      if (this.sign == another.sign) {
         //question, is this correct?
         this.genuineAdd(another, sum);
         //the sign is going to be + or - depending on what sum has
         sum.sign = this.sign;
         return sum;

         //if they both have different signs
      } else {
         //if this is larger, it will determine the sign and will be the minuend
         if (greater(this, another) == this) {
            this.genuineSub(another, sum);
            //takes the sign of the larger value
            sum.sign = this.sign;
            return sum;
         } else if (greater(this, another) == another) {
            //subtract this from another, the answer will be put into sum
            another.genuineSub(this, sum);
            //takes the sign of the larger value
            sum.sign = another.sign;
            return sum;
            //if they were the same and one was negative, the sum will be 0
         } else {
            //the answer will be 0, so the length only needs to be 1
            sum.value = new int[1];
            sum.value[0] = 0;
            sum.sign = 1;
            return sum;
         }
      }
   }

   //storing it in sum, so not return value
   //alters sum does not return anything
   public void genuineAdd(BigInt another, BigInt sum) {
      int carry = 0;
      //stringRes consists of all the columns added togehter
      String stringRes = "";
      int addColumns;
      //if they were equal to, it doesn't matter what the length is
      if (this.value.length >= another.value.length) {
         addColumns = this.value.length;
      } else {
         addColumns = another.value.length;
      }
      for (int i = 0; i < addColumns; i++) {
         //since you don't know which one is shorter or longer
         //just check to make sure there are valid digits in both
         if (i < this.value.length && i < another.value.length) {
            //add the numbers plus carry, then mod by MODCONSTANT
            //make mod to operate specifically with the sum of this column
            int mod = (this.value[i] + another.value[i] + carry) % MODCONSTANT;
            //we add it to the string
            stringRes = mod + stringRes;
            //if it is 0, all the digits will be 0
            if (mod == 0) {
               stringRes = "000" + stringRes;
               //if it isn't 0, add the padded 0
            } else {
               while (mod * 10 < MODCONSTANT) {
                  stringRes = "0" + stringRes;
                  mod *= 10;
               }
            }
            if ((this.value[i] + another.value[i] + carry) >= MODCONSTANT) {
               carry = 1;
            } else {
               carry = 0;
            }
            //we have ran through all of the digits of one integer
         } else if (this.value.length == addColumns) {
            stringRes = ((this.value[i] + carry) % MODCONSTANT) + stringRes;
            int mod = ((this.value[i] + carry) % MODCONSTANT);
            if (mod == 0) {
               stringRes = "000" + stringRes;
               //if it isn't 0, add the padded 0
            } else {
               while (mod < MODCONSTANT / 10) {
                  stringRes = "0" + stringRes;
                  mod *= 10;
               }
            }
            if ((this.value[i] + carry) >= MODCONSTANT) {
               carry = 1;
            } else {
               carry = 0;
            }

            //if another == addColumn
            //another 
         } else {
            //carry has to be carried all the way through
            int mod = ((another.value[i] + carry) % MODCONSTANT);
            stringRes = mod + stringRes;
            if (mod == 0) {
               stringRes = "000" + stringRes;
               //if it isn't 0, add the padded 0
            } else {
               while (mod < MODCONSTANT / 10) {
                  stringRes = "0" + stringRes;
                  mod *= 10;
               }
            }
            if (another.value[i] + carry >= MODCONSTANT) {
               carry = 1;
            } else {
               carry = 0;
            }
         }
         change++;
      }

      if (carry == 1) {
         stringRes = "1" + stringRes;
      }
      sum.toBigInt(stringRes);

   }

   public static BigInt greater(BigInt compare1, BigInt compare2) {
      //as long as the  last digit of compare1 is 0 and the length is not 1
      //ex. 100(which in reality is 001) not 0
	   while(compare1.value[compare1.value.length - 1] == 0 && compare1.value.length != 1) {
	      //make a copy of compare1's array`
		   int[] temp = compare1.value;
		   //makes a new array attribute of compare 1 that is 1 digit shorter
		   compare1.value = new int [compare1.value.length - 1];
		   //substitute the values in for copmare 1
		   for(int i = 0 ; i < compare1.value.length; i++) {
			   compare1.value[i] = temp[i];
		   }
	   }
	   while(compare2.value[compare2.value.length - 1] == 0 && compare2.value.length != 1) {
		   int[] temp = compare2.value;
		   compare1.value = new int [compare2.value.length - 1];
		   for(int i = 0 ; i < compare2.value.length; i++) {
			   compare2.value[i] = temp[i];
		   }
	   }
	   
      if (compare1.value.length > compare2.value.length) {
         return compare1;
      } else if (compare1.value.length < compare2.value.length) {
         return compare2;
      } else {
         //go through both arrays, starting with the most significant digits
         for (int i = compare1.value.length - 1; i >= 0; i--) {
            if (compare1.value[i] > compare2.value[i]) {
               return compare1;
            } else if (compare1.value[i] < compare2.value[i]) {
               return compare2;
            }
         }

      }
      //if both are equal
      return null;
   }

   public void toBigInt(String digits) {
      int check = digits.length();
      for (int j = 0; j < check; j++) {
         if (digits.charAt(0) != 0) {
            break;
         } else {
            digits = digits.substring(1, digits.length());
         }
      }
      int beginIndex = 0;
      //if it is not divisible by 4, you will have an extra space for the remainder
      if (digits.length() % 4 != 0) {
         value = new int[digits.length() / 4 + 1];
         //if it is, you don't need an extra space for remainder
      } else {
         value = new int[digits.length() / 4];
      }

      //use x to count the spaces in the array, because you skip by 4 in the string
      int x = 0;
      //begin at either 0 or 1 depending on sign
      //insert 4 digits at a time
      for (int i = beginIndex; i < digits.length(); i = i + 4) {

         //if there are 4 or more digits left
         if (digits.length() - i >= 4) {
            //insert the 4 numbers prior to the last number you were at
            value[x] = (int) (Long.parseLong(digits.substring(digits.length() - i - 4, digits.length() - i)));

            //for the other elses, if there are less than 3 digits, insert into one array space
         } else if (digits.length() - i == 3) {
            value[x] = (int) (Long.parseLong(digits.substring(0, 3)));

         } else if (digits.length() - i == 2) {
            value[x] = (int) (Long.parseLong(digits.substring(0, 2)));

         } else {
            value[x] = (int) (Long.parseLong(digits.substring(0, 1)));
         }
         //go to the next space in the array
         x++;
      }
   }

   public BigInt subtract(BigInt another) {
      BigInt difference = new BigInt();
      //if postive - negative
      if (this.sign == 1 && another.sign == -1) {
         this.genuineAdd(another, difference);
         difference.sign = 1;
         return difference;

         ///if negative-positive
      } else if (this.sign == -1 && another.sign == 1) {
         this.genuineAdd(another, difference);
         difference.sign = -1;
         return difference;

         //if positive minus postive
      } else if (this.sign == 1 && another.sign == 1) {

         //if minued is larger, subtract and sign = 1
         if (greater(this, another) == this) {
            //subtract another from this
            this.genuineSub(another, difference);
            difference.sign = 1;
            return difference;

            //if the subtrahend is larger, subtract and result will be negative
         } else if (greater(this, another) == another) {
            another.genuineSub(this, difference);
            difference.sign = -1;
            return difference;

            //if they are equal, just 0
         } else {
            difference.value = new int[1];
            difference.value[0] = 0;
            difference.sign = 1;
            return difference;

         }
         //if both signs are negative
      } else {
         //if the first number is larger
         if (greater(this, another) == this) {
            //subtract another from this
            this.genuineSub(another, difference);
            difference.sign = -1;
            return difference;

         } else if (greater(this, another) == another) {
            //subtract this from another
            another.genuineSub(this, difference);
            difference.sign = 1;
            return difference;

            //if they are equal
         } else {
            difference.value = new int[1];
            difference.value[0] = 0;
            difference.sign = 1;
            return difference;
         }
      }

   }

   public void genuineSub(BigInt another, BigInt difference) {
      int extra = 0;
      int subColumns = another.value.length;
      String dif = "";


      for (int i = 0; i < this.value.length; i++) {
         //compare their lengths
         if (i < subColumns) {
            //mod is the result
            //because we are subtracting. we will never be greater
            int mod = (this.value[i] - another.value[i] - extra);
            if (mod < 0) {
               extra = 1;
               mod = mod + MODCONSTANT;
            } else {
               extra = 0;

            }
            //updating the string
            dif = mod + dif;
            if (mod == 0) {
               dif = "000" + dif;
            } else {
               while (mod * 10 < MODCONSTANT) {
                  dif = "0" + dif;
                  mod *= 10;
               }
            }
         } else {
            int mod = this.value[i] - extra;
            if (mod < 0) {
               extra = 1;
               mod = mod + MODCONSTANT;
            } else {
               extra = 0;

            }
            dif = mod + dif;
            if (mod == 0) {
               dif = "000" + dif;
            } else {
               while (mod * 10 < MODCONSTANT) {
                  dif = "0" + dif;
                  mod *= 10;
               }
            }
            //change++;
         }
         difference.toBigInt(dif);
         change++;
      }
   }

   public BigInt multiply(BigInt another) {
      //result what we will return
      BigInt res = new BigInt("0");

      //set our signs
      if (this.sign == another.sign) {
         res.sign = 1;
      } else {
         res.sign = -1;
      }
      //res holds all the values 
      //starting at one, because the add method updates its size
      BigInt bigIntProduct = new BigInt();
      //goes through the values in the bottom row
      for (int i = 0; i < another.value.length; i++) {
         //goes through top row
         for (int x = 0; x < this.value.length; x++) {
            String specificProduct = (this.value[x] * another.value[i]) + "";

               for(int j = 0; j < i; j++){
                  specificProduct = specificProduct + "0000";
               }
               for(int a = 0; a < x; a++){
                  specificProduct = specificProduct + "0000";
               }
            
            //specific to the column, but is of bigInt type
            bigIntProduct.toBigInt(specificProduct);
            //if you are at the last digit, that is accurate of its value
            //updates res with the new addition
            //running total array res
            res.genuineAdd(bigIntProduct, res);
         }
      }
      return res;

   }

   public String toString() {

      String stringValue = "";
      for (int i = 0; i < value.length; i++) {
         stringValue = this.value[i] + stringValue;
         int temp = this.value[i];
         if (i + 1 < value.length) {
            if (temp != 0) {
               while (temp * 10 < MODCONSTANT) {
                  stringValue = "0" + stringValue;
                  temp *= 10;
               }
            } else {
               stringValue = "000" + stringValue;
            }
         }
      }
      if (this.sign == -1) {
         stringValue = "-" + stringValue;
      }
      return stringValue;
   }

   public BigInt divide(BigInt another) {
      BigInt divresult = new BigInt("0");
      BigInt increase = new BigInt("1");

      BigInt updatingSubResult = new BigInt(this.toString());
      //this integer will update every time division is performed
      
      updatingSubResult.sign = 1;
      
      while (greater(updatingSubResult, another) == updatingSubResult) {
         //updates subResult
         updatingSubResult = updatingSubResult.subtract(another);
         divresult = divresult.add(increase);
         //change++;
         //System.out.println(count);
      }
      
      if(greater(updatingSubResult, another) == null){
         divresult = divresult.add(increase);
         //change++;
         //System.out.println(count);
      }
      
      if (this.sign == 1 && another.sign == 1 || this.sign == -1 && another.sign == -1) {
         divresult.sign = 1;
      } else {
         divresult.sign = -1;
      }
      System.out.println(change);
      return divresult;
   }
}