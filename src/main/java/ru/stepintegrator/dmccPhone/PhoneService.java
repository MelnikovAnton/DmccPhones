package ru.stepintegrator.dmccPhone;

import ch.ecma.csta.binding.*;
import ch.ecma.csta.callcontrol.CallControlAdapter;
import ch.ecma.csta.callcontrol.CallControlServices;
import ch.ecma.csta.errors.CstaException;
import ch.ecma.csta.monitor.MonitoringServices;
import ch.ecma.csta.physical.PhysicalDeviceServices;
import com.avaya.csta.binding.*;
import com.avaya.csta.binding.types.DependencyMode;
import com.avaya.csta.binding.types.DeviceInstance;
import com.avaya.csta.binding.types.MediaMode;
import com.avaya.csta.device.DeviceServices;
import com.avaya.csta.registration.RegistrationServices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PhoneService {

    private static final String SWITH_NAME = "cm02";
    private static final Boolean SHARED_CONTROL = true;
    private static final String PHONE_PROPERTIES_FILE_NAME = "phones.properties";

    private DeviceServices deviceServices;
    private RegistrationServices registrationServices;
    private MonitoringServices monitoringServices;
    private final CallControlServices callControlServices = DMCC.getCallControlServices();

    public ConnectionID connection = null;

    private String pass;


    public void init() throws IOException {
        deviceServices = DMCC.getDeviceServices();
        registrationServices = DMCC.getRegistrationServices();

        monitoringServices = DMCC.getMonitoringServices();

        Properties props = loadProperty(PHONE_PROPERTIES_FILE_NAME);

        this.pass = props.getProperty("password");
        List<String> extensions = parseExt(props.getProperty("extensions"));
        for (String ext : extensions) {
            registerDevice(ext, pass);
        }


    }


    private List<String> parseExt(String ext) {
        List<String> extensions = new ArrayList<>();
        String[] arr1 = ext.split(",");
        for (String set : arr1) {
            String[] arr2 = set.split("-");
            if (arr2.length == 1) extensions.add(arr2[0]);
            else {
                int min = Integer.parseInt(arr2[0]);
                int max = Integer.parseInt(arr2[1]);
                for (int i = min; i <= max; i++) {
                    extensions.add(String.valueOf(i));
                }
            }

        }

        return extensions;
    }

    private void registerDevice(String ext, String pass) {
        //------Getting Device ID-----------
        GetDeviceId devIDReq = new GetDeviceId();
        devIDReq.setSwitchName(SWITH_NAME);
        devIDReq.setExtension(ext);

        devIDReq.setDeviceInstance(DeviceInstance.VALUE_0);

        DeviceID deviceID = null;
        devIDReq.setControllableByOtherSessions(Boolean.TRUE);//for cti us
        try {
            deviceID = deviceServices.getDeviceID(devIDReq).getDevice();
        } catch (CstaException e) {

            e.printStackTrace();
        }



        try {
            monitoringServices.addCallControlListener(deviceID, new MyCallControllListener());
        } catch (CstaException e) {
            e.printStackTrace();
        }

        connection = new ConnectionID();
        LocalDeviceID lid = new LocalDeviceID();
        lid.setStaticID(deviceID);
        connection.setDeviceID(lid);

        System.out.println("Device id is " + deviceID);

        try {
            monitoringServices.addCallControlListener(deviceID, new MyCallControllListener());
        } catch (CstaException e) {
            e.printStackTrace();
        }
        //-----Loggin info -----
        LoginInfo login = new LoginInfo();
        login.setPassword(pass);
     //   login.setSharedControl(SHARED_CONTROL);
        //   login.setDependencyMode(DependencyMode.MAIN);
        //login.setMediaMode(MediaMode.SERVER);

        RegisterTerminalRequest regTermReq = new RegisterTerminalRequest();

        regTermReq.setLoginInfo(login);
        regTermReq.setDevice(deviceID);

        //Registring device
        RegisterTerminalResponse resp = null;
        try {

            resp = registrationServices.registerTerminal(regTermReq);
            Integer code = resp.getCode();
            String reason = resp.getReason();

            System.out.println("*********************** Registration Response *********************************");
            System.out.println("Registration code is " + code);
            System.out.println("Registration Reason " + reason);
            System.out.println("*********************** Registration Response *********************************");

        } catch (CstaException e) {
            System.out.println("*********************** Registration Response *********************************");
            System.out.println(e.getCSTAErrorCode().toString());
            System.out.println("*********************** Registration Response *********************************");

        }

//
//        DeviceID finalDeviceID = deviceID;
//        new Thread(() -> {
//            while (true) {
//                try {
//                    startMonitorDevice(finalDeviceID);
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
        MakeCall request = new MakeCall();
        request.setCallingDevice(deviceID);
        GetThirdPartyDeviceId getExDecID = new GetThirdPartyDeviceId();
        getExDecID.setExtension("62301");
        getExDecID.setSwitchName(SWITH_NAME);
        DeviceID calledDeviceId = null;
        try {
            GetThirdPartyDeviceIdResponse respDi = deviceServices.getThirdPartyDeviceID(getExDecID);
            calledDeviceId=respDi.getDevice();
        } catch (CstaException e) {
            e.printStackTrace();
        }
        request.setCalledDirectoryNumber(calledDeviceId);
        try {
            callControlServices.makeCall(request);
        } catch (CstaException e) {
            e.printStackTrace();
        }


    }

    private void startMonitorDevice(DeviceID deviceID) throws CstaException {
        PhysicalDeviceServices phyDevSrv = DMCC.getPhysicalDeviceServices();
        GetDisplay req = new GetDisplay();
        req.setDevice(deviceID);
        GetDisplayResponse resp = phyDevSrv.getDisplay(req);
        DisplayList dList = resp.getDisplayList();
        DisplayListItem[] items = dList.getDisplayListItem();
        String content = "";
        for (DisplayListItem item : items) {
            if (!content.equals(item.getContentsOfDisplay())) {

            content=item.getContentsOfDisplay();
                System.out.println(content);
            }

        }
    }

    private static Properties loadProperty(String propertiesFileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(propertiesFileName)) {
            properties.load(inputStream);
        }
        return properties;
    }

}
