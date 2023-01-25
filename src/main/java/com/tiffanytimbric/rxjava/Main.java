package com.tiffanytimbric.rxjava;

import rx.Observable;
import rx.Single;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


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
 *          type is analogous to event stream, event processing.
 *         </blockquote>
 *     </li>
 *     <li>
 *         <b>Pull:</b>
 *         <blockquote>
 *          A fixed block of data pre-exists before the processing of it begins.
 *          This flow type is analogous to Java Collections.
 *         </blockquote>
 *     </li>
 * </ul>
 * <p><p>
 * The Reactive eXtensions API supports two methods of data consumption,
 * blocking read, and subscription.  Blocking read blocks the reading thread.
 * Subscription read may be processed by a separate thread, thereby not blocking
 * the Flow construction thread.  In general, event handlers (push data flow)
 * use subscriptions instead of blocking reads.
 */
public final class Main {

    public static void main( @Nullable String... args ) {
        System.out.println( "Program Start\n" );

        System.out.println( "Pulling data..." );
        Observable<Integer> allNumbersObs = createPullTheDataObs();
        System.out.println( "Extracting data as list from Pull Data Flux..." );
        List<Integer> allNumbers = extractAsList( allNumbersObs );
        System.out.println( "All Numbers: " + allNumbers );

        System.out.println( "Pulling data..." );
        allNumbersObs = createPullTheDataObs();
        System.out.println( "Subscribing to Pull Data Flux..." );
        allNumbersObs.subscribe( System.out::println );

        System.out.println( "\nPushing data..." );
        allNumbersObs = createPushTheDataObs();
        allNumbersObs.subscribe( System.out::println );

        System.out.println( "\nProgram End" );
    }

    @Nonnull
    private static List<Integer> extractAsList(
            @Nonnull final Observable<Integer> allNumbersObs
    ) {
        return allNumbersObs
                .toList()
                .toBlocking()
                .singleOrDefault( new ArrayList<>() );
    }

    @Nonnull
    private static Observable<Integer> createPullTheDataObs() {
        final Single<Integer> zeroSingle = Single.just( 0 );
        final Observable<Integer> oddNumbersObs = Observable.just( 1, 3, 5, 7 );
        final Observable<Integer> evenNumbersObs = Observable.just( 2, 4, 6, 8 );

        return zeroSingle.toObservable()
                .mergeWith( oddNumbersObs )
                .mergeWith( evenNumbersObs );
    }

    @Nonnull
    private static Observable<Integer> createPushTheDataObs() {
        return Observable.empty();
    }

}
