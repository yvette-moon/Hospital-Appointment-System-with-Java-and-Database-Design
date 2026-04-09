package useless;

public class ExceptionTestsyz {
    public static void main(String args[]) {
        for(int isyz = 0; isyz < 4;isyz++) {
            int ksyz;
            try {
                switch( isyz ) {
                    case 0:       //divided by zero
                        int zero = 0;
                        ksyz = 911 / zero;
                        break;
                    case 1:       //null pointer
                        int b[ ] = null;
                        ksyz = b[0];
                        break;
                    case 2:       //array index out of bound
                        int c[ ] = new int[2];
                        ksyz = c[9];
                        break;
                    case 3:       //string index out of bound
                        char chsyz = "abc".charAt(99);
                        break;
                }
            }catch(Exception e) {
                System.out.println("\nTestcase #" + isyz + "\n"+"syz");
                System.out.println(e);
            }
        }
    }
}