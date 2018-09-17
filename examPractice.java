
public class examPractice {
    public int x = 0;

    public examPractice(int x) {
        this.x = x;
    }

    public class InnerChild extends examPractice {
        public int x = 1;

        public InnerChild(int x) {
            super(2 * x);
            this.x = x;
        }

        public void innerMethod(int x) {
            outerMethod(x);
            System.out.println(x);
            System.out.println(this.x);
            System.out.println(super.x);
            System.out.println(examPractice.this.x);
        }
    }

    public void outerMethod(int x) {
        System.out.println(x);
        System.out.println(this.x);
    }

    public static void main(String[] args) {
        int x = 37;
        examPractice p = new examPractice(x);
        x = 6;
        examPractice c1 = p.new InnerChild(x + 1);
        InnerChild c2 = p.new InnerChild(x - 1);
        p.outerMethod(2);
        c1.outerMethod(3);
        c2.innerMethod(4);
        //p.innerMethod(5);
        //((InnerChild)p).innerMethod(5);
        //c1.innerMethod(5);
        //((InnerChild)c1).innerMethod(5);
        //c2.outerMethod(5);
        //examPractice p2 = p;
        //examPractice p2 = c2;
        //examPractice p2 = new examPractice(10);
        //examPractice p2 = new examPractice(10).new InnerChild(20);
        //InnerChild c3 = p;
        //InnerChild c3 = c2;
        //InnerChild c3 = c2.new InnerChild(20);
        //InnerChild c3 = new examPractice(10);
        InnerChild c3 = new examPractice(10).new InnerChild(20);
    }
}
