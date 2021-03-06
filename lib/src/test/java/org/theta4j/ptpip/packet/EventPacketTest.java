/*
 * Copyright (C) 2015 theta4j project
 */

package org.theta4j.ptpip.packet;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.theta4j.ptp.io.PtpInputStream;
import org.theta4j.ptp.type.UINT16;
import org.theta4j.ptp.type.UINT32;
import org.theta4j.util.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class EventPacketTest {
    private static final byte[] PAYLOAD = new byte[UINT16.SIZE_IN_BYTES + UINT32.SIZE_IN_BYTES + UINT32.SIZE_IN_BYTES * 3];
    private static final UINT16 EVENT_CODE = new UINT16(0);
    private static final UINT32 TRANSACTION_ID = new UINT32(0);
    private static final UINT32 P1 = new UINT32(1);
    private static final UINT32 P2 = new UINT32(2);
    private static final UINT32 P3 = new UINT32(3);

    public static class Construct {
        @Test(expected = NullPointerException.class)
        public void withNullEventCode() {
            // act
            new EventPacket(null, TRANSACTION_ID, P1, P2, P3);
        }

        @Test(expected = NullPointerException.class)
        public void withNullTransactionID() {
            // act
            new EventPacket(EVENT_CODE, null, P1, P2, P3);
        }

        @Test(expected = NullPointerException.class)
        public void withNullParam1() {
            // act
            new EventPacket(EVENT_CODE, TRANSACTION_ID, null, P2, P3);
        }

        @Test(expected = NullPointerException.class)
        public void withNullParam2() {
            // act
            new EventPacket(EVENT_CODE, TRANSACTION_ID, P1, null, P3);
        }

        @Test(expected = NullPointerException.class)
        public void withNullParam3() {
            // act
            new EventPacket(EVENT_CODE, TRANSACTION_ID, P1, P2, null);
        }

        @Test
        public void andGet() {
            // expected
            byte[] expectedPayload = ArrayUtils.join(
                    EVENT_CODE.bytes(),
                    TRANSACTION_ID.bytes(),
                    P1.bytes(), P2.bytes(), P3.bytes()
            );

            // act
            EventPacket packet = new EventPacket(EVENT_CODE, TRANSACTION_ID, P1, P2, P3);

            // verify
            assertThat(packet.getType(), Is.is(PtpIpPacket.Type.EVENT));
            assertThat(packet.getEventCode(), is(EVENT_CODE));
            assertThat(packet.getTransactionID(), is(TRANSACTION_ID));
            assertThat(packet.getP1(), is(P1));
            assertThat(packet.getP2(), is(P2));
            assertThat(packet.getP3(), is(P3));
            assertThat(packet.getPayload(), is(expectedPayload));
        }
    }

    public static class Read {
        @Test(expected = NullPointerException.class)
        public void nullValue() throws IOException {
            // act
            EventPacket.read(null);
        }

        @Test(expected = IOException.class)
        public void invalidType() throws IOException {
            // given
            PtpIpPacket.Type invalidType = PtpIpPacket.Type.INIT_EVENT_REQUEST;

            // arrange
            byte[] givenPacketBytes = PtpIpPacketTestUtils.bytes(invalidType, PAYLOAD);
            PtpInputStream givenInputStream = new PtpInputStream(new ByteArrayInputStream(givenPacketBytes));

            // act
            EventPacket.read(givenInputStream);
        }

        @Test(expected = IOException.class)
        public void invalidLengthPayload() throws IOException {
            // given
            byte[] givenPayload = new byte[PAYLOAD.length - 1];  // expected length - 1

            // arrange
            byte[] givenPacketBytes = PtpIpPacketTestUtils.bytes(PtpIpPacket.Type.EVENT, givenPayload);
            PtpInputStream givenInputStream = new PtpInputStream(new ByteArrayInputStream(givenPacketBytes));

            // act
            EventPacket.read(givenInputStream);
        }

        @Test
        public void normal() throws IOException {
            // given
            byte[] givenPayload = ArrayUtils.join(
                    EVENT_CODE.bytes(),
                    TRANSACTION_ID.bytes(),
                    P1.bytes(), P2.bytes(), P3.bytes()
            );

            // arrange
            byte[] givenPacketBytes = PtpIpPacketTestUtils.bytes(PtpIpPacket.Type.EVENT, givenPayload);
            PtpInputStream givenInputStream = new PtpInputStream(new ByteArrayInputStream(givenPacketBytes));

            // act
            EventPacket actual = EventPacket.read(givenInputStream);

            // verify
            assertThat(actual.getType(), Is.is(PtpIpPacket.Type.EVENT));
            assertThat(actual.getEventCode(), is(EVENT_CODE));
            assertThat(actual.getTransactionID(), is(TRANSACTION_ID));
            assertThat(actual.getP1(), is(P1));
            assertThat(actual.getP2(), is(P2));
            assertThat(actual.getP3(), is(P3));
            assertThat(actual.getPayload(), is(givenPayload));
        }
    }

    public static class HashCode {
        @Test
        public void ofDifferentEventCode() {
            // given
            EventPacket packet1 = new EventPacket(new UINT16(0), TRANSACTION_ID);
            EventPacket packet2 = new EventPacket(new UINT16(1), TRANSACTION_ID);

            // verify
            assertThat(packet1.hashCode(), not(packet2.hashCode()));
        }

        @Test
        public void ofDifferentTransactionID() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, new UINT32(1));

            // verify
            assertThat(packet1.hashCode(), not(packet2.hashCode()));
        }

        @Test
        public void ofDifferentP1() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    new UINT32(1));

            // verify
            assertThat(packet1.hashCode(), not(packet2.hashCode()));
        }

        @Test
        public void ofDifferentP2() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, new UINT32(1));

            // verify
            assertThat(packet1.hashCode(), not(packet2.hashCode()));
        }

        @Test
        public void ofDifferentP3() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, P2, new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, P2, new UINT32(1));

            // verify
            assertThat(packet1.hashCode(), not(packet2.hashCode()));
        }

        @Test
        public void ofSameValues() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID);
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID);

            // verify
            assertThat(packet1.hashCode(), is(packet2.hashCode()));
        }
    }

    public static class NotEquals {
        @Test
        public void withNull() {
            // given
            EventPacket packet = new EventPacket(EVENT_CODE, TRANSACTION_ID);

            // verify
            assertFalse(packet.equals(null));
        }

        @Test
        public void withDifferentClass() {
            // given
            EventPacket packet = new EventPacket(EVENT_CODE, TRANSACTION_ID);

            // verify
            assertFalse(packet.equals("foo"));
        }

        @Test
        public void withEventCode() {
            // given
            EventPacket packet1 = new EventPacket(new UINT16(0), TRANSACTION_ID);
            EventPacket packet2 = new EventPacket(new UINT16(1), TRANSACTION_ID);

            // verify
            assertFalse(packet1.equals(packet2));
        }

        @Test
        public void withTransactionID() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, new UINT32(1));

            // verify
            assertFalse(packet1.equals(packet2));
        }

        @Test
        public void withP1() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    new UINT32(1));

            // verify
            assertFalse(packet1.equals(packet2));
        }

        @Test
        public void withP2() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, new UINT32(1));

            // verify
            assertFalse(packet1.equals(packet2));
        }

        @Test
        public void withP3() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, P2, new UINT32(0));
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, P2, new UINT32(1));

            // verify
            assertFalse(packet1.equals(packet2));
        }
    }

    public static class Equals {
        @Test
        public void withSameInstances() {
            // given
            EventPacket packet = new EventPacket(EVENT_CODE, TRANSACTION_ID);

            // verify
            assertTrue(packet.equals(packet));
        }

        @Test
        public void withSameValues() {
            // given
            EventPacket packet1 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, P2, P3);
            EventPacket packet2 = new EventPacket(EVENT_CODE, TRANSACTION_ID,
                    P1, P2, P3);

            // verify
            assertTrue(packet1.equals(packet2));
        }
    }

    public static class ToString {
        @Test
        public void normal() {
            // given
            EventPacket packet = new EventPacket(EVENT_CODE, TRANSACTION_ID);

            // act
            String actual = packet.toString();

            // verify
            assertTrue(actual.contains(packet.getClass().getSimpleName()));
            assertTrue(actual.contains("eventCode"));
            assertTrue(actual.contains("transactionID"));
            assertTrue(actual.contains("p1"));
            assertTrue(actual.contains("p2"));
            assertTrue(actual.contains("p3"));
        }
    }
}
