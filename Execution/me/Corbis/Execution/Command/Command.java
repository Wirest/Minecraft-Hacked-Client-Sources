package me.Corbis.Execution.Command;

public class Command {

    String name;
    String syntax;
    int numberOfArguments;

    public Command(String name, String syntax, int numberOfArguments) {
        this.name = name;
        this.syntax = syntax;
        this.numberOfArguments = numberOfArguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public void setNumberOfArguments(int numberOfArguments) {
        this.numberOfArguments = numberOfArguments;
    }

    public void onCommand(String command, String[] args){

    }
}
