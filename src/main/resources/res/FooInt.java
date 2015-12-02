package res;

public class FooInt {
    int a;

    public void bar(int j) {
        int  i = 1 + 9;
        i = 9 + 11;
        j = 5 + 15;
        this.a = 10 + j;
    }
    
    public void bar(final String j) {
    }

    public int getA() {
        return this.a;
    }
}
