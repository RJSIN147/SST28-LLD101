/**
 * Simulated paid external API service.
 * 
 * In a real system, this would make an HTTP call to a third-party API
 * (e.g., a geocoding service, payment processor, AI model endpoint).
 */
public class PaidApiService implements ExternalService {

    private final String serviceName;

    public PaidApiService(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String call(String request) {
        // Simulate external API call
        return "[" + serviceName + "] Response for: " + request;
    }

    @Override
    public String toString() {
        return "PaidApiService{" + serviceName + "}";
    }
}
