package qa.dcsdr.diplomaticclub.Tools;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Tamim on 6/19/2015.
 */


/**
 * Created by Tamim on 6/12/2015.
 */
public abstract class HidingScrollViewListener implements ViewTreeObserver.OnScrollChangedListener {

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private View context;

    protected HidingScrollViewListener(View context) {
        this.context = context;
    }

    public abstract void onHide();
    public abstract void onShow();

    public void onScrollChanged() {
        if (scrolledDistance == 0) {
            if(!controlsVisible) {
                onShow();
                controlsVisible = true;
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }
        }

        if((controlsVisible && context.getScrollY()>0) || (!controlsVisible && context.getScrollY()<0)) {
            scrolledDistance += context.getScrollY();
        }
    }

}