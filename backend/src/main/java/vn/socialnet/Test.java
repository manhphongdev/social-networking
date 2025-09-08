package vn.socialnet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Test implements CommandLineRunner {
    private DataSource ds;

    @Override
    public void run(String... args) throws Exception {
        String s1 = new String("hello");
        String s2 = new String("hello");

        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));


    }


}
