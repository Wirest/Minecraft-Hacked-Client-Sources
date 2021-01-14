package com.thealtening.auth;

import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.ServiceSwitcher;

public final class TheAlteningAuthentication {
    private static TheAlteningAuthentication instance;
    private final ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
    private final SSLController sslController = new SSLController();
    private AlteningServiceType service;

    private TheAlteningAuthentication(AlteningServiceType paramAlteningServiceType) {
        updateService(paramAlteningServiceType);
    }

    public static TheAlteningAuthentication mojang() {
        return withService(AlteningServiceType.MOJANG);
    }

    public static TheAlteningAuthentication theAltening() {
        return withService(AlteningServiceType.THEALTENING);
    }

    private static TheAlteningAuthentication withService(AlteningServiceType paramAlteningServiceType) {
        if (instance == null) {
            instance = new TheAlteningAuthentication(paramAlteningServiceType);
        } else if (instance.getService() != paramAlteningServiceType) {
            instance.updateService(paramAlteningServiceType);
        }
        return instance;
    }

    public void updateService(AlteningServiceType paramAlteningServiceType) {
        if ((paramAlteningServiceType == null) || (this.service == paramAlteningServiceType)) {
            return;
        }
        switch (paramAlteningServiceType) {
            case MOJANG:
                this.sslController.enableCertificateValidation();
                break;
            case THEALTENING:
                this.sslController.disableCertificateValidation();
        }
        this.service = this.serviceSwitcher.switchToService(paramAlteningServiceType);
    }

    public AlteningServiceType getService() {
        return this.service;
    }
}




