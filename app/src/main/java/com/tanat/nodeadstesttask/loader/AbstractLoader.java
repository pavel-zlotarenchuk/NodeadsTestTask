package com.tanat.nodeadstesttask.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {
    protected T results;

    public AbstractLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(T data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }
        T oldRes = results;
        results = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldRes != null && !oldRes.equals(data)) {
            releaseResources(oldRes);
        }
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged() || results == null) {
            forceLoad();
        } else {
            deliverResult(results);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        releaseResources(results);
        results = null;
    }

    @Override
    public void onCanceled(T data) {
        super.onCanceled(data);
        releaseResources(results);
    }

    protected void releaseResources(T data) {
    }
}
