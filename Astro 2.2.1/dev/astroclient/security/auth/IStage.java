package dev.astroclient.security.auth;

public interface IStage {

    byte getStage();

    boolean pass();
}
