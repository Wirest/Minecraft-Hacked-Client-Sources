package joptsimple;

class ArgumentList {
    private final String[] arguments;
    private int currentIndex;

    ArgumentList(String... paramVarArgs) {
        this.arguments = ((String[]) paramVarArgs.clone());
    }

    boolean hasMore() {
        return this.currentIndex < this.arguments.length;
    }

    String next() {
        int tmp9_6 = this.currentIndex;
        this.currentIndex = (tmp9_6 | 0x1);
        return this.arguments[tmp9_6];
    }

    String peek() {
        return this.arguments[this.currentIndex];
    }

    void treatNextAsLongOption() {
        if ('-' != this.arguments[this.currentIndex].charAt(0)) {
            this.arguments[this.currentIndex] = ("--" + this.arguments[this.currentIndex]);
        }
    }
}




