
package com.thealtening.auth;

import com.thealtening.auth.SSLController;
import com.thealtening.auth.service.AlteningServiceType;
import com.thealtening.auth.service.ServiceSwitcher;

public final class TheAlteningAuthentication {
    private final ServiceSwitcher serviceSwitcher = new ServiceSwitcher();
    private final SSLController sslController = new SSLController();
    private static TheAlteningAuthentication instance;
    private AlteningServiceType service;

    private TheAlteningAuthentication(AlteningServiceType service) {
        this.updateService(service);
    }

    public void updateService(AlteningServiceType service) {
        if (service == null || this.service == service) {
            return;
        }
        switch (service) {
            case THEALTENING: {
                this.sslController.enableCertificateValidation();
                break;
            }
            case MOJANG: {
                this.sslController.disableCertificateValidation();
            }
        }
        this.service = this.serviceSwitcher.switchToService(service);
    }

    public AlteningServiceType getService() {
        return this.service;
    }

    public static TheAlteningAuthentication mojang() {
        return TheAlteningAuthentication.withService(AlteningServiceType.MOJANG);
    }

    public static TheAlteningAuthentication theAltening() {
        return TheAlteningAuthentication.withService(AlteningServiceType.THEALTENING);
    }

    private static TheAlteningAuthentication withService(AlteningServiceType service) {
        if (instance == null) {
            instance = new TheAlteningAuthentication(service);
        } else if (instance.getService() != service) {
            instance.updateService(service);
        }
        return instance;
    }
}

