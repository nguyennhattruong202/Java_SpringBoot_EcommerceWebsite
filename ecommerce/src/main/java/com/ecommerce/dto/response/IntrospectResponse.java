package com.ecommerce.dto.response;

public class IntrospectResponse {

    private boolean valid;

    public IntrospectResponse() {
    }

    public IntrospectResponse(boolean valid) {
        this.valid = valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean getValid() {
        return this.valid;
    }
}
