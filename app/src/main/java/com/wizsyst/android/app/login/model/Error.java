package com.wizsyst.android.app.login.model;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class Error {

    private String code,
                   description,
                   solution;

    public Error() {}

    public Error( String code, String description ) {

        this.code = code;
        this.description = description;
    }

    public Error( String code, String description, String solution ) {

        this( code, description );
        this.solution = solution;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error = (Error) o;

        if (!code.equals(error.code)) return false;
        if (description != null ? !description.equals(error.description) : error.description != null)
            return false;
        return solution != null ? solution.equals(error.solution) : error.solution == null;

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (solution != null ? solution.hashCode() : 0);
        return result;
    }
}
