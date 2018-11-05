package application;

import java.io.IOException;

public class GA_Main {

    public static void run(int flag) throws IOException, InterruptedException {
        new GA();
        new Install(flag);
    }
}
