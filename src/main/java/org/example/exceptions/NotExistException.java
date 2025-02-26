package org.example.exceptions;

public class NotExistException extends RepositoryException {
    public NotExistException(String message) {
        super(message);
    }
}
