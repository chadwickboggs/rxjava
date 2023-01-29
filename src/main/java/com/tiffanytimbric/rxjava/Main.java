package com.tiffanytimbric.rxjava;

import rx.Observable;
import rx.Single;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


/**
 * The Reactive eXtensions API supports two methods of data flow, push and pull.
 * The Java Streams API support supports only one method of data flow, pull.
 * Reactive Streams defines pull data flow as a "cold" publisher, and the push
 * data flow as a "hot" publisher.
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

    public static final int PUBLISH_SLEEP_BOUND = 501;
    public static final int PUBLISH_MAX_VALUE = 8;

    private static final Random random = new Random( System.currentTimeMillis() );

    public static void main( @Nullable String... args ) {
        System.out.println( "Program Start\n" );

        demoDataPullWithBlockingRead();
        System.out.println();
        demoDataPullWithSubscriptionRead();
        System.out.println();
        demoDataPushWithSubscriptionRead();
        System.out.println();
        demoDataPushWithSubscriptionReadSorted();

        System.out.println( "\nProgram End" );
    }

    private static void demoDataPullWithBlockingRead() {
        System.out.println( "Demoing data pull with blocking read..." );

        System.out.println( "Pulling data..." );
        final Observable<Integer> allNumbersObs = createPullTheDataObs();

        System.out.println( "Extracting data as list from Pull Data Flux..." );
        List<Integer> allNumbers = extractAsList( allNumbersObs );
        System.out.println( "All Numbers: " + allNumbers );
    }

    private static void demoDataPullWithSubscriptionRead() {
        System.out.println( "Demoing data pull with subscription read..." );

        System.out.println( "Pulling data..." );
        final Observable<Integer> allNumbersObs = createPullTheDataObs();

        System.out.println( "Subscribing to Pull Data Flux..." );
        allNumbersObs.subscribe( System.out::println );
    }

    private static void demoDataPushWithSubscriptionRead() {
        System.out.println( "Demoing data push with subscription read..." );

        System.out.println( "\nPushing data..." );
        final Observable<Integer> allNumbersObs = createPushTheDataObs( PUBLISH_MAX_VALUE );

        System.out.println( "Subscribing to Push Data Flux..." );
        allNumbersObs.subscribe( System.out::println );
        allNumbersObs.publish().connect();

        sleep( PUBLISH_SLEEP_BOUND * ( PUBLISH_MAX_VALUE * 3 / 4 ) );
    }

    private static void demoDataPushWithSubscriptionReadSorted() {
        System.out.println( "Demoing data push with subscription read sorted..." );

        System.out.println( "\nPushing data..." );
        final Observable<Integer> allNumbersObs = createPushTheDataObs( PUBLISH_MAX_VALUE );
        final Observable<Integer> allNumbersSortedObs = allNumbersObs
                .buffer( PUBLISH_MAX_VALUE + 1 )
                .map( Main::sort )
                .flatMapIterable( numbers -> numbers );

        System.out.println( "Subscribing to Push Data Sorted Flux..." );
        allNumbersSortedObs.subscribe( System.out::println );
        allNumbersSortedObs.publish().connect();

        sleep( PUBLISH_SLEEP_BOUND * ( PUBLISH_MAX_VALUE * 3 / 4 ) );
    }

    @Nonnull
    private static List<Integer> sort( @Nonnull final List<Integer> numbers ) {
        Collections.sort( numbers );

        return numbers;
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
    private static Observable<Integer> createPushTheDataObs( int maxValue ) {
        final Single<Integer> zeroMono = Single.just( 0 );

        final Observable<Integer> oddNumbersObs = Observable.create( emitter -> new Thread( () -> {
            for ( int count = 1; count <= maxValue; count += 2 ) {
                sleep( random.nextInt( PUBLISH_SLEEP_BOUND ) );

                emitter.onNext( count );
            }
        } ).start() );
        final Observable<Integer> evenNumbersObs = Observable.create( emitter -> new Thread( () -> {
            for ( int count = 2; count <= maxValue; count += 2 ) {
                sleep( random.nextInt( PUBLISH_SLEEP_BOUND ) );

                emitter.onNext( count );
            }
        } ).start() );

        return zeroMono.toObservable()
                .mergeWith( oddNumbersObs )
                .mergeWith( evenNumbersObs );
    }

    private static void sleep( int millis ) {
        try {
            Thread.sleep( millis );
        } catch ( Throwable ignored ) {
        }
    }

}
