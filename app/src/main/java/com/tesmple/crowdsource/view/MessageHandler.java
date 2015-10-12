package com.tesmple.crowdsource.view;

/**
 * Created by lypeer on 10/12/2015.
 */
import android.os.Handler;
import android.os.Message;

// Referenced classes of package com.qingchifan.view:
//            LoopView

final class MessageHandler extends Handler {

    final LoopView a;

    MessageHandler(LoopView loopview) {
        super();
        a = loopview;
    }

    public final void handleMessage(Message paramMessage) {
        if (paramMessage.what == 1000)
            this.a.invalidate();
        while (true) {
            if (paramMessage.what == 2000)
                LoopView.b(a);
            else if (paramMessage.what == 3000)
                this.a.c();
            super.handleMessage(paramMessage);
            return;
        }
    }

}