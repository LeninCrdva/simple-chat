package tec.edu.azuay.chat.exceptions;

public class ExistsObjectException extends RuntimeException{

    public ExistsObjectException(){
        super("Object already exists");
    }
}
