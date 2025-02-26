package org.example.exceptions;

public class DuplicateException extends RepositoryException {
    public DuplicateException(String message) {
        super(message);
    }
}
