package ru.stepintegrator.dmccPhone;


import ch.ecma.csta.binding.DeviceID;
import ch.ecma.csta.callcontrol.CallControlServices;
import ch.ecma.csta.logical.LogicalDeviceFeatureServices;
import ch.ecma.csta.media.MediaServices;
import ch.ecma.csta.monitor.MonitoringServices;
import ch.ecma.csta.physical.PhysicalDeviceServices;
import ch.ecma.csta.voiceunit.VoiceUnitServices;
import com.avaya.cmapi.APIProtocolVersion;
import com.avaya.cmapi.ServiceProvider;
import com.avaya.csta.binding.*;
import com.avaya.csta.device.DeviceServices;
import com.avaya.csta.registration.RegistrationServices;
import com.avaya.mvcs.framework.CmapiKeys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DMCC {

    private static final Log log = LogFactory.getLog(DMCC.class);
    private static final String dmccPropertiesFileName = "dmcc.properties";

    private static DeviceServices deviceServices;

    private static CallControlServices callControlServices;

    private static LogicalDeviceFeatureServices logicalDeviceFeatureServices;

    private static MonitoringServices monitoringServices;

    private static PhysicalDeviceServices physicalDeviceServices;

    private static RegistrationServices registrationServices;



    private static MediaServices mediaServices;

    private static VoiceUnitServices voiceSvcs;

    public static void main(String[] args) throws Exception {
        Properties properties = loadProperty(dmccPropertiesFileName);
        System.out.println("6.3"+APIProtocolVersion.VERSION_6_3);
        System.out.println("6.3.3"+APIProtocolVersion.VERSION_6_3_3);

        ServiceProvider serviceProvider = ServiceProvider.getServiceProvider(properties);
        System.out.println(serviceProvider.getSessionID());

        deviceServices = deviceServices(serviceProvider);
        callControlServices = callControlServices(serviceProvider);
        monitoringServices = monitoringServices(serviceProvider);
        physicalDeviceServices = physicalDeviceServices(serviceProvider);
        registrationServices = registrationServices(serviceProvider);
    //    mediaServices=mediaServices(serviceProvider);
voiceSvcs=voiceUnitServices(serviceProvider);
logicalDeviceFeatureServices=logicalDeviceFeatureServices(serviceProvider);

        new PhoneService().init();
    }

    //---------------------------getters----------------

    public static DeviceServices getDeviceServices() {
        return deviceServices;
    }

    public static CallControlServices getCallControlServices() {
        return callControlServices;
    }

    public static LogicalDeviceFeatureServices getLogicalDeviceFeatureServices() {
        return logicalDeviceFeatureServices;
    }

    public static MonitoringServices getMonitoringServices() {
        return monitoringServices;
    }

    public static PhysicalDeviceServices getPhysicalDeviceServices() {
        return physicalDeviceServices;
    }

    public static RegistrationServices getRegistrationServices() {
        return registrationServices;
    }

    public static MediaServices getMediaServices() {
        return mediaServices;
    }

    public static VoiceUnitServices getVoiceSvcs() {
        return voiceSvcs;
    }

    ///------------------------------------services----------------

    private static DeviceServices deviceServices(ServiceProvider serviceProvider) throws Exception {
        return (DeviceServices) serviceProvider.getService(com.avaya.csta.device.DeviceServices.class.getName());
    }


    private static CallControlServices callControlServices(ServiceProvider serviceProvider) throws Exception {
        return (CallControlServices) serviceProvider.getService(CallControlServices.class.getName());
    }

    private static LogicalDeviceFeatureServices logicalDeviceFeatureServices(ServiceProvider serviceProvider) throws Exception {
        return (LogicalDeviceFeatureServices) serviceProvider.getService(LogicalDeviceFeatureServices.class.getName());
    }

    private static MonitoringServices monitoringServices(ServiceProvider serviceProvider) throws Exception {
        return (MonitoringServices) serviceProvider.getService(ch.ecma.csta.monitor.MonitoringServices.class.getName());
    }

    private static PhysicalDeviceServices physicalDeviceServices(ServiceProvider serviceProvider) throws Exception {
        return (PhysicalDeviceServices) serviceProvider.getService(ch.ecma.csta.physical.PhysicalDeviceServices.class.getName());
    }

    private static RegistrationServices registrationServices(ServiceProvider serviceProvider) throws Exception {
        return (RegistrationServices) serviceProvider.getService(com.avaya.csta.registration.RegistrationServices.class.getName());
    }

//    private static MediaServices mediaServices(ServiceProvider serviceProvider) throws Exception {
//        return (MediaServices) serviceProvider.getService(ch.ecma.csta.media.MediaServices.class.getName());
//    }

    private static VoiceUnitServices voiceUnitServices(ServiceProvider serviceProvider) throws Exception {
        return (VoiceUnitServices) serviceProvider.getService(ch.ecma.csta.voiceunit.VoiceUnitServices.class.getName());
    }

    private static Properties loadProperty(String dmccPropertiesFileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(dmccPropertiesFileName)) {
            properties.load(inputStream);
        }
        return properties;
    }

}


