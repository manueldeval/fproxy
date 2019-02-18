package org.deman.fproxy.utils;

import io.vavr.CheckedFunction0;
import io.vavr.control.Option;

public class Env {

    public static Option<Integer> toInteger(String s){
        return CheckedFunction0.lift(() -> Integer.valueOf(s)).apply();
    }

    public static Option<Integer> getInteger(String envname){
        return getValue(envname)
            .flatMap(Env::toInteger);
    }

    public static Integer getInteger(String envname, Integer other){
        return getInteger(envname)
            .getOrElse(other);
    }

    public static Option<String> getString(String envname) {
        return getValue(envname);
    }


    public static String getString(String envname, String s) {
        return getString(envname)
            .getOrElse(s);
    }

    public static Option<String> getValue(String key){
        return getInEnv(key)
            .orElse(() -> getInProperty(key));
    }

    public static Option<String> getInEnv(String key){
        return Option.of(System.getenv(key));
    }

    public static Option<String> getInProperty(String key){
        return Option.of(System.getProperty(key));
    }
}
