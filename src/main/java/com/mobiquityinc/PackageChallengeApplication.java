package com.mobiquityinc;

import com.mobiquityinc.processor.CalculatePacker;
import com.mobiquityinc.exception.APIException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PackageChallengeApplication {

    public static void main(String[] args) throws APIException {
        SpringApplication.run(PackageChallengeApplication.class, args);

        if (args.length != 1) {
            System.err.println("Input file path must provider as args[0]");
            System.exit(1);
        }

        System.out.println(new CalculatePacker().pack(args[0]));
    }

}
