package com.africastalking;


/**
 * Communicates responses from a server. One and only one method will be
 * invoked in response to a given request.
 * @param <T> expected result type
 */
public interface Callback<T> {

    /**
     * Invoked for a received response.
     * @param data T Received data
     */
    void onSuccess(T data);


    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     * @param throwable Thrown error or exception
     */
    void onFailure(Throwable throwable);

}
