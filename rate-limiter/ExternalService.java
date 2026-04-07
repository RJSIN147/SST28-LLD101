/**
 * Interface representing an external (paid) resource.
 * 
 * Implementations wrap actual external API calls.
 * This abstraction allows us to place a rate-limiting proxy
 * in front of any external service transparently.
 */
public interface ExternalService {

    /**
     * Calls the external resource with the given request payload.
     *
     * @param request the request data
     * @return the response from the external service
     */
    String call(String request);
}
