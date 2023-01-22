package com.tiffanytimbric.rxjava;

import rx.Observable;
import rx.Single;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;


public final class Main {

    public static void main( @Nullable String... args ) {
        System.out.println("Program Start");

        final Single<Integer> zeroSingle = Single.just( 0 );
        final Observable<Integer> oddNumbersObs = Observable.just( 1, 3, 5, 7 );
        final Observable<Integer> evenNumbersObs = Observable.just( 2, 4, 6, 8 );

        final Observable<List<Integer>> allNumbersObs = zeroSingle
            .toObservable()
            .mergeWith( oddNumbersObs )
            .mergeWith( evenNumbersObs )
//            .subscribe( System.out::println )
            .toList();
        final List<Integer> allNumbers = allNumbersObs
            .toBlocking()
            .singleOrDefault( new ArrayList<>() );

        System.out.println( "All Numbers: " + allNumbers );

        System.out.println("Program End");
    }

}
