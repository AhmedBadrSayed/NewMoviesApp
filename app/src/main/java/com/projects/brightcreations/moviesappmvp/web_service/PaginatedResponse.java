package com.projects.brightcreations.moviesappmvp.web_service;

/**
 * @author ahmed on 27/03/18.
 */

public class PaginatedResponse<S> extends BaseResponse {

    S[] results;

    public S[] getResults() {
        return results;
    }

    public void setResults(S[] results) {
        this.results = results;
    }
}
