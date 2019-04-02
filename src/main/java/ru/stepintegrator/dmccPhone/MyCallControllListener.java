package ru.stepintegrator.dmccPhone;

import ch.ecma.csta.binding.*;
import ch.ecma.csta.callcontrol.CallControlListener;
import ch.ecma.csta.callcontrol.CallControlServices;
import ch.ecma.csta.errors.CstaException;
import ch.ecma.csta.media.MediaServices;
import ch.ecma.csta.voiceunit.VoiceUnitServices;
import com.avaya.csta.binding.EnteredDigitsEvent;
import com.avaya.csta.binding.PlayMessagePrivateData;

public class MyCallControllListener implements CallControlListener {
//
//    private final CallControlServices callControlServices=DMCC.getCallControlServices();
//    private final VoiceUnitServices voiceUnitServices=DMCC.getVoiceSvcs();
//
//    private ConnectionID connection;
//
//
//    public MyCallControllListener(ConnectionID connection) {
//        this.connection = connection;
//    }

    @Override
    public void bridged(BridgedEvent bridgedEvent) {
        System.out.println("bridged");
    }

    @Override
    public void callCleared(CallClearedEvent callClearedEvent) {
        System.out.println("callCleared");
    }

    @Override
    public void conferenced(ConferencedEvent conferencedEvent) {
        System.out.println("conferenced");
    }

    @Override
    public void connectionCleared(ConnectionClearedEvent connectionClearedEvent) {
       }

    @Override
    public void delivered(DeliveredEvent deliveredEvent) {
        System.out.println("delivered");
        System.out.println(deliveredEvent.getCallingDevice().getDeviceIdentifier().getExtension());
//        AnswerCall req =new AnswerCall();
//        req.setCallToBeAnswered(deliveredEvent.getConnection());
//        AnswerCallResponse resp=null;
////        try {
////            Thread.sleep(2000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        try {
//             resp = callControlServices.answerCall(req);
//        } catch (CstaException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void digitsDialed(DigitsDialedEvent digitsDialedEvent) {
        System.out.println("digitsDialed");
    }

    @Override
    public void diverted(DivertedEvent divertedEvent) {
        System.out.println("diverted");
    }

    @Override
    public void established(EstablishedEvent establishedEvent) {
        System.out.println("established");
        System.out.println(establishedEvent.getCallingDevice().getDeviceIdentifier().getExtension());
    }

    @Override
    public void failed(FailedEvent failedEvent) {
        System.out.println("failed");
    }

    @Override
    public void held(HeldEvent heldEvent) {
        System.out.println("held");
    }

    @Override
    public void networkCapabilitiesChanged(NetworkCapabilitiesChangedEvent networkCapabilitiesChangedEvent) {
        System.out.println("networkCapabilitiesChanged");
    }

    @Override
    public void networkReached(NetworkReachedEvent networkReachedEvent) {
        System.out.println("networkReached");
    }

    @Override
    public void offered(OfferedEvent offeredEvent) {
        System.out.println("offered");
    }

    @Override
    public void originated(OriginatedEvent originatedEvent) {
        System.out.println("originated");
    }

    @Override
    public void queued(QueuedEvent queuedEvent) {
        System.out.println("queued");
    }

    @Override
    public void retrieved(RetrievedEvent retrievedEvent) {
        System.out.println("retrieved");
    }

    @Override
    public void serviceInitiated(ServiceInitiatedEvent serviceInitiatedEvent) {
        System.out.println("serviceInitiated");
    }

    @Override
    public void transferred(TransferedEvent transferedEvent) {
        System.out.println("transferred");
    }

    @Override
    public void enteredDigits(EnteredDigitsEvent enteredDigitsEvent) {
        System.out.println("enteredDigits");
    }

    @Override
    public void terminated() {
        System.out.println("terminated");
    }
}
