package org.deman.fproxy.utils;

import io.vavr.*;
import io.vavr.control.Option;

public class FP {

    public static <A1, A2, T> Option<T> mapAllOptions(Option<A1> a1,
                                                      Option<A2> a2,
                                                      Function2<A1, A2, T> mapper) {
        return a1.flatMap(a1o ->
            a2.map(a2o -> mapper.apply(a1o, a2o)));
    }

    public static <A1, A2, A3, T> Option<T> mapAllOptions(Option<A1> a1,
                                                          Option<A2> a2,
                                                          Option<A3> a3,
                                                          Function3<A1, A2, A3, T> mapper) {
        return a1.flatMap(a1o ->
            a2.flatMap(a2o ->
                a3.map(a3o -> mapper.apply(a1o, a2o, a3o))));
    }

    public static <A1, A2, A3, A4, T> Option<T> mapAllOptions(Option<A1> a1,
                                                              Option<A2> a2,
                                                              Option<A3> a3,
                                                              Option<A4> a4,
                                                              Function4<A1, A2, A3, A4, T> mapper) {
        return a1.flatMap(a1o ->
            a2.flatMap(a2o ->
                a3.flatMap(a3o ->
                    a4.map(a4o -> mapper.apply(a1o, a2o, a3o, a4o)))));
    }

    public static <A1, A2, A3, A4, A5, T> Option<T> mapAllOptions(Option<A1> a1,
                                                                  Option<A2> a2,
                                                                  Option<A3> a3,
                                                                  Option<A4> a4,
                                                                  Option<A5> a5,
                                                                  Function5<A1, A2, A3, A4, A5, T> mapper) {
        return a1.flatMap(a1o ->
            a2.flatMap(a2o ->
                a3.flatMap(a3o ->
                    a4.flatMap(a4o ->
                        a5.map(a5o -> mapper.apply(a1o, a2o, a3o, a4o, a5o))))));
    }


    public static <A1, A2, A3, A4, A5, A6, T> Option<T> mapAllOptions(Option<A1> a1,
                                                                      Option<A2> a2,
                                                                      Option<A3> a3,
                                                                      Option<A4> a4,
                                                                      Option<A5> a5,
                                                                      Option<A6> a6,
                                                                      Function6<A1, A2, A3, A4, A5, A6, T> mapper) {
        return a1.flatMap(a1o ->
            a2.flatMap(a2o ->
                a3.flatMap(a3o ->
                    a4.flatMap(a4o ->
                        a5.flatMap(a5o ->
                            a6.map(a6o -> mapper.apply(a1o, a2o, a3o, a4o, a5o, a6o)))))));
    }
}
