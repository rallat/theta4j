/*
 * Copyright (C) 2015 theta4j project
 */

package org.theta4j.ptpip.packet;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.theta4j.ptp.io.PtpInputStream;
import org.theta4j.ptp.type.UINT32;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.theta4j.ptpip.packet.PtpIpPacket.Type.CANCEL;
import static org.theta4j.ptpip.packet.PtpIpPacket.Type.INIT_EVENT_REQUEST;

@RunWith(Enclosed.class)
public class CancelPacketTest {
    private static final byte[] PAYLOAD = new byte[UINT32.SIZE_IN_BYTES];
    private static final UINT32 TRANSACTION_ID = new UINT32(0);

    public static class Construct {
        @Test(expected = NullPointerException.class)
        public void withNullTransactionID() {
            // act
            new CancelPacket(null);
        }

        @Test
        public void andGet() {
            // expected
            byte[] expectedPayload = TRANSACTION_ID.bytes();

            // act
            CancelPacket packet = new CancelPacket(TRANSACTION_ID);

            // verify
            assertThat(packet.getType(), is(CANCEL));
            assertThat(packet.getTransactionID(), is(TRANSACTION_ID));
            assertThat(packet.getPayload(), is(expectedPayload));
        }
    }

    public static class Read {
        @Test(expected = NullPointerException.class)
        public void nullValue() throws IOException {
            // act
            CancelPacket.read(null);
        }

        @Test(expected = IOException.class)
        public void invalidType() throws IOException {
            // given
            PtpIpPacket.Type invalidType = INIT_EVENT_REQUEST;

            // arrange
            byte[] givenPacketBytes = PtpIpPacketTestUtils.bytes(invalidType, PAYLOAD);
            PtpInputStream givenInputStream = new PtpInputStream(new ByteArrayInputStream(givenPacketBytes));

            // act
            CancelPacket.read(givenInputStream);
        }

        @Test(expected = IOException.class)
        public void invalidLengthPayload() throws IOException {
            // given
            byte[] givenPayload = new byte[PAYLOAD.length - 1];  // expected length - 1

            // arrange
            byte[] givenPacketBytes = PtpIpPacketTestUtils.bytes(CANCEL, givenPayload);
            PtpInputStream givenInputStream = new PtpInputStream(new ByteArrayInputStream(givenPacketBytes));

            // act
            CancelPacket.read(givenInputStream);
        }

        @Test
        public void normal() throws IOException {
            // given
            byte[] givenPayload = TRANSACTION_ID.bytes();

            // arrange
            byte[] givenPacketBytes = PtpIpPacketTestUtils.bytes(CANCEL, givenPayload);
            PtpInputStream givenInputStream = new PtpInputStream(new ByteArrayInputStream(givenPacketBytes));

            // act
            CancelPacket actual = CancelPacket.read(givenInputStream);

            // verify
            assertThat(actual.getType(), is(CANCEL));
            assertThat(actual.getTransactionID(), is(TRANSACTION_ID));
            assertThat(actual.getPayload(), is(givenPayload));
        }
    }

    public static class HashCode {
        @Test
        public void ofDifferentTransactionID() {
            // given
            CancelPacket packet1 = new CancelPacket(new UINT32(0));
            CancelPacket packet2 = new CancelPacket(new UINT32(1));

            // verify
            assertThat(packet1.hashCode(), not(packet2.hashCode()));
        }

        @Test
        public void ofSameValues() {
            // given
            CancelPacket packet1 = new CancelPacket(TRANSACTION_ID);
            CancelPacket packet2 = new CancelPacket(TRANSACTION_ID);

            // verify
            assertThat(packet1.hashCode(), is(packet2.hashCode()));
        }
    }

    public static class NotEquals {
        @Test
        public void withNull() {
            // given
            CancelPacket packet = new CancelPacket(new UINT32(0));

            // verify
            assertFalse(packet.equals(null));
        }

        @Test
        public void withDifferentClass() {
            // given
            CancelPacket packet = new CancelPacket(TRANSACTION_ID);

            // verify
            assertFalse(packet.equals("foo"));
        }

        @Test
        public void withTransactionID() {
            // given
            CancelPacket packet1 = new CancelPacket(new UINT32(0));
            CancelPacket packet2 = new CancelPacket(new UINT32(1));

            // verify
            assertFalse(packet1.equals(packet2));
        }
    }

    public static class Equals {
        @Test
        public void withSameInstances() {
            // given
            CancelPacket packet = new CancelPacket(TRANSACTION_ID);

            // verify
            assertTrue(packet.equals(packet));
        }

        @Test
        public void withSameValues() {
            // given
            CancelPacket packet1 = new CancelPacket(TRANSACTION_ID);
            CancelPacket packet2 = new CancelPacket(TRANSACTION_ID);

            // verify
            assertTrue(packet1.equals(packet2));
        }
    }

    public static class ToString {
        @Test
        public void normal() {
            // given
            CancelPacket packet = new CancelPacket(TRANSACTION_ID);

            // act
            String actual = packet.toString();

            // verify
            assertTrue(actual.contains(packet.getClass().getSimpleName()));
            assertTrue(actual.contains("transactionID"));
        }
    }
}
