package org.theta4j;

import org.theta4j.ptp.PtpException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GetDevicePropDescTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetDevicePropDescTest.class);

    private static ThetaEventListener listener = new ThetaEventListener() {
        @Override
        public void onObjectAdded(long objectHandle) {
            LOGGER.info("onObjectAdded: " + objectHandle);
        }

        @Override
        public void onCaptureStatusChanged() {
            LOGGER.info("onCaptureStatusChanged");
        }

        @Override
        public void onRecordingTimeChanged() {
            LOGGER.info("onRecordingTimeChanged");
        }

        @Override
        public void onRemainingRecordingTimeChanged() {
            LOGGER.info("onRemainingRecordingTimeChanged");
        }

        @Override
        public void onStoreFull(long storageID) {
            LOGGER.info("onStoreFull: " + storageID);
        }

        @Override
        public void onCaptureComplete(long transactionID) {
            LOGGER.info("onCaptureComplete: " + transactionID);
        }
    };

    private static Theta theta;

    @BeforeClass
    public static void connect() throws IOException, PtpException {
        theta = new Theta();
        theta.addListener(listener);
    }

    @AfterClass
    public static void close() throws IOException, InterruptedException, PtpException {
        theta.close();
        Thread.sleep(TestParameters.INTERVAL_MS);
    }

    // Get DevicePropDesc

    @Test
    public void getBatteryLevel() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of BatteryLevel: " + theta.getBatteryLevel());
    }

    @Test
    public void getWhiteBalance() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of WhiteBalance: " + theta.getWhiteBalance());
    }

    @Test
    public void getExposureIndex() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of ExposureIndex: " + theta.getExposureIndex());
    }

    @Test
    public void getExposureBiasCompensation() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of ExposureBiasCompensation: " + theta.getExposureBiasCompensation());
    }

    @Test
    public void getDateTime() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of DateTime: " + theta.getDateTime());
    }

    @Test
    public void getStillCaptureMode() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of StillCaptureMode(: " + theta.getStillCaptureMode());
    }

    @Test
    public void getTimelapseNumber() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of TimelapseNumber: " + theta.getTimelapseNumber());
    }

    @Test
    public void getTimelapseInterval() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of TimelapseInterval: " + theta.getTimelapseInterval());
    }

    @Test
    public void getAudioVolume() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of AudioVolume: " + theta.getAudioVolume());
    }

    @Test
    public void getErrorInfo() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of ErrorInfo: " + theta.getErrorInfo());
    }

    @Test
    public void getShutterSpeed() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of ShutterSpeed: " + theta.getShutterSpeed());
    }

    @Test
    public void getGpsInfo() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of GpsInfo: " + theta.getGPSInfo());
    }

    @Test
    public void getAutoPowerOffDelay() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of AutoPowerOffDelay: " + theta.getAutoPowerOffDelay());
    }

    @Test
    public void getSleepDelay() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of SleepDelay: " + theta.getSleepDelay());
    }

    @Test
    public void getChannelNumber() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of ChannelNumber: " + theta.getChannelNumber());
    }

    @Test
    public void getCaptureStatus() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of CaptureStatus: " + theta.getCaptureStatus());
    }

    @Test
    public void getRecordingTime() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of RecordingTime: " + theta.getRecordingTime());
    }

    @Test
    public void getRemainingRecordingTime() throws IOException, PtpException {
        LOGGER.info("DevicePropDesc of RemainingRecordingTime: " + theta.getRemainingRecordingTime());
    }
}