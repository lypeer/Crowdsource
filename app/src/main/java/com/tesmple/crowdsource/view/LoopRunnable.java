package com.tesmple.crowdsource.view;

/**
 * Created by lypeer on 10/12/2015.
 */
final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    public final void run() {
        LoopListener listener = loopView.loopListener;
        int i = LoopView.a(loopView);
        loopView.arrayList.get(LoopView.a(loopView));
        listener.onItemSelect(i);
    }
}