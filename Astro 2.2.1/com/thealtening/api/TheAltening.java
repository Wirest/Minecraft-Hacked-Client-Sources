package com.thealtening.api;

import com.thealtening.api.retriever.AsynchronousDataRetriever;
import com.thealtening.api.retriever.BasicDataRetriever;

public final class TheAltening {

    public static BasicDataRetriever newBasicRetriever(String apiKey) {
        return new BasicDataRetriever(apiKey);
    }

    public static AsynchronousDataRetriever newAsyncRetriever(String apiKey) {
        return new AsynchronousDataRetriever(apiKey);
    }
}
