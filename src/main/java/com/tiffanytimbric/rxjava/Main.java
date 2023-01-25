package com.tiffanytimbric.rxjava;

import rx.Observable;
import rx.Single;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * The Reactive eXtensions API supports two methods of data flow, push and pull.
 * The Java Streams API support supports only one method of data flow, pull.
 * <p><p>
 * <h3>Data Flow Types</h3>
 * <ul>
 *     <li>
 *         <b>Push:</b>
 *         <blockquote>
 *          New data arrives asynchronously until the publisher signals its end.
 *          The subscriber process the incoming data as it arrives.  This flow
 *          type is alloigous to event stream, event processing.
 *         </blockquote>
 *     </li>
 *     <li>
 *         <b>Pull:</b>
 *         <blockquote>
 *          A fixed block of data pre-exists before the processing of it begins.
 *          This flow type is analigous to Java Collections.
 *         </blockquote>
 *     </li>
 * </ul>
 */
public final class Main {

    public static void main( @Nullable String... args ) {
        System.out.println("Program Start\n");

        System.out.println("Pulling data...");
        List<Integer> allNumbers = pullTheData();
        System.out.println( "All Numbers: " + allNumbers );

        System.out.println("\nPushing data...");
        allNumbers = pushTheData();
        System.out.println( "All Numbers: " + allNumbers );

        System.out.println("\nProgram End");
    }

    @Nonnull
    private static List<Integer> pullTheData() {
        final Single<Integer> zeroSingle = Single.just( 0 );
        final Observable<Integer> oddNumbersObs = Observable.just( 1, 3, 5, 7 );
        final Observable<Integer> evenNumbersObs = Observable.just( 2, 4, 6, 8 );

        final Observable<List<Integer>> allNumbersObs = zeroSingle
            .toObservable()
            .mergeWith( oddNumbersObs )
            .mergeWith( evenNumbersObs )
//            .subscribe( System.out::println )
            .toList();

        return allNumbersObs
            .toBlocking()
            .singleOrDefault( new ArrayList<>() );
    }

    @Nonnull
    private static List<Integer> pushTheData() {
        return new ArrayList<>();
    }

}
