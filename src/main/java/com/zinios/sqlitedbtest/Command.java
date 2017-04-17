package com.zinios.sqlitedbtest;

public class Command {

    public String[] instruction;
    public String[] otherCommands;
    public String workingDir;

    public Command(String[] instruction, String workingDir) {
        this.instruction = instruction;
        this.workingDir = workingDir;
    }

    public void setOtherCommands(String[] otherCommands) {
        this.otherCommands = otherCommands;
    }

}
