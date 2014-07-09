/**
 * This class is automatically generated by mig. DO NOT EDIT THIS FILE.
 * This class implements a Java interface to the 'DozerTh3TempMsg'
 * message type.
 */

package ch.ethz.permasense.tinyos2x.messages;

public class DozerTh3TempMsg extends ch.ethz.permasense.tinyos2x.messages.DataHeaderMsg {

    /** The default size of this message type in bytes. */
    public static final int DEFAULT_MESSAGE_SIZE = 21;

    /** The Active Message type associated with this message. */
    public static final int AM_TYPE = 180;

    /** Create a new DozerTh3TempMsg of size 21. */
    public DozerTh3TempMsg() {
        super(DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /** Create a new DozerTh3TempMsg of the given data_length. */
    public DozerTh3TempMsg(int data_length) {
        super(data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerTh3TempMsg with the given data_length
     * and base offset.
     */
    public DozerTh3TempMsg(int data_length, int base_offset) {
        super(data_length, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerTh3TempMsg using the given byte array
     * as backing store.
     */
    public DozerTh3TempMsg(byte[] data) {
        super(data);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerTh3TempMsg using the given byte array
     * as backing store, with the given base offset.
     */
    public DozerTh3TempMsg(byte[] data, int base_offset) {
        super(data, base_offset);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerTh3TempMsg using the given byte array
     * as backing store, with the given base offset and data length.
     */
    public DozerTh3TempMsg(byte[] data, int base_offset, int data_length) {
        super(data, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerTh3TempMsg embedded in the given message
     * at the given base offset.
     */
    public DozerTh3TempMsg(net.tinyos.message.Message msg, int base_offset) {
        super(msg, base_offset, DEFAULT_MESSAGE_SIZE);
        amTypeSet(AM_TYPE);
    }

    /**
     * Create a new DozerTh3TempMsg embedded in the given message
     * at the given base offset and length.
     */
    public DozerTh3TempMsg(net.tinyos.message.Message msg, int base_offset, int data_length) {
        super(msg, base_offset, data_length);
        amTypeSet(AM_TYPE);
    }

    /**
    /* Return a String representation of this message. Includes the
     * message type name and the non-indexed field values.
     */
    public String toString() {
      String s = "Message <DozerTh3TempMsg> \n";
      try {
        s += "  [header.seqNr=0x"+Long.toHexString(get_header_seqNr())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [header.originatorID=0x"+Long.toHexString(get_header_originatorID())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [header.aTime.low=0x"+Long.toHexString(get_header_aTime_low())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [header.aTime.high=0x"+Long.toHexString(get_header_aTime_high())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sample.valid=0x"+Long.toHexString(get_payload_sample_valid())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.sample.no=0x"+Long.toHexString(get_payload_sample_no())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.t1=0x"+Long.toHexString(get_payload_t1())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.t2=0x"+Long.toHexString(get_payload_t2())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.t3=0x"+Long.toHexString(get_payload_t3())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.t4=0x"+Long.toHexString(get_payload_t4())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.t5=0x"+Long.toHexString(get_payload_t5())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      try {
        s += "  [payload.t6=0x"+Long.toHexString(get_payload_t6())+"]\n";
      } catch (ArrayIndexOutOfBoundsException aioobe) { /* Skip field */ }
      return s;
    }

    // Message-type-specific access methods appear below.

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.seqNr
    //   Field type: int
    //   Offset (bits): 0
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.seqNr' is signed (false).
     */
    public static boolean isSigned_header_seqNr() {
        return false;
    }

    /**
     * Return whether the field 'header.seqNr' is an array (false).
     */
    public static boolean isArray_header_seqNr() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.seqNr'
     */
    public static int offset_header_seqNr() {
        return (0 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.seqNr'
     */
    public static int offsetBits_header_seqNr() {
        return 0;
    }

    /**
     * Return the value (as a int) of the field 'header.seqNr'
     */
    public int get_header_seqNr() {
        return (int)getUIntBEElement(offsetBits_header_seqNr(), 16);
    }

    /**
     * Set the value of the field 'header.seqNr'
     */
    public void set_header_seqNr(int value) {
        setUIntBEElement(offsetBits_header_seqNr(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.seqNr'
     */
    public static int size_header_seqNr() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.seqNr'
     */
    public static int sizeBits_header_seqNr() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.originatorID
    //   Field type: int
    //   Offset (bits): 16
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.originatorID' is signed (false).
     */
    public static boolean isSigned_header_originatorID() {
        return false;
    }

    /**
     * Return whether the field 'header.originatorID' is an array (false).
     */
    public static boolean isArray_header_originatorID() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.originatorID'
     */
    public static int offset_header_originatorID() {
        return (16 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.originatorID'
     */
    public static int offsetBits_header_originatorID() {
        return 16;
    }

    /**
     * Return the value (as a int) of the field 'header.originatorID'
     */
    public int get_header_originatorID() {
        return (int)getUIntBEElement(offsetBits_header_originatorID(), 16);
    }

    /**
     * Set the value of the field 'header.originatorID'
     */
    public void set_header_originatorID(int value) {
        setUIntBEElement(offsetBits_header_originatorID(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.originatorID'
     */
    public static int size_header_originatorID() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.originatorID'
     */
    public static int sizeBits_header_originatorID() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.aTime.low
    //   Field type: int
    //   Offset (bits): 32
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.aTime.low' is signed (false).
     */
    public static boolean isSigned_header_aTime_low() {
        return false;
    }

    /**
     * Return whether the field 'header.aTime.low' is an array (false).
     */
    public static boolean isArray_header_aTime_low() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.aTime.low'
     */
    public static int offset_header_aTime_low() {
        return (32 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.aTime.low'
     */
    public static int offsetBits_header_aTime_low() {
        return 32;
    }

    /**
     * Return the value (as a int) of the field 'header.aTime.low'
     */
    public int get_header_aTime_low() {
        return (int)getUIntBEElement(offsetBits_header_aTime_low(), 16);
    }

    /**
     * Set the value of the field 'header.aTime.low'
     */
    public void set_header_aTime_low(int value) {
        setUIntBEElement(offsetBits_header_aTime_low(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.aTime.low'
     */
    public static int size_header_aTime_low() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.aTime.low'
     */
    public static int sizeBits_header_aTime_low() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: header.aTime.high
    //   Field type: short
    //   Offset (bits): 48
    //   Size (bits): 8
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'header.aTime.high' is signed (false).
     */
    public static boolean isSigned_header_aTime_high() {
        return false;
    }

    /**
     * Return whether the field 'header.aTime.high' is an array (false).
     */
    public static boolean isArray_header_aTime_high() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'header.aTime.high'
     */
    public static int offset_header_aTime_high() {
        return (48 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'header.aTime.high'
     */
    public static int offsetBits_header_aTime_high() {
        return 48;
    }

    /**
     * Return the value (as a short) of the field 'header.aTime.high'
     */
    public short get_header_aTime_high() {
        return (short)getUIntBEElement(offsetBits_header_aTime_high(), 8);
    }

    /**
     * Set the value of the field 'header.aTime.high'
     */
    public void set_header_aTime_high(short value) {
        setUIntBEElement(offsetBits_header_aTime_high(), 8, value);
    }

    /**
     * Return the size, in bytes, of the field 'header.aTime.high'
     */
    public static int size_header_aTime_high() {
        return (8 / 8);
    }

    /**
     * Return the size, in bits, of the field 'header.aTime.high'
     */
    public static int sizeBits_header_aTime_high() {
        return 8;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sample.valid
    //   Field type: byte
    //   Offset (bits): 56
    //   Size (bits): 1
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sample.valid' is signed (false).
     */
    public static boolean isSigned_payload_sample_valid() {
        return false;
    }

    /**
     * Return whether the field 'payload.sample.valid' is an array (false).
     */
    public static boolean isArray_payload_sample_valid() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sample.valid'
     */
    public static int offset_payload_sample_valid() {
        return (56 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sample.valid'
     */
    public static int offsetBits_payload_sample_valid() {
        return 56;
    }

    /**
     * Return the value (as a byte) of the field 'payload.sample.valid'
     */
    public byte get_payload_sample_valid() {
        return (byte)getUIntBEElement(offsetBits_payload_sample_valid(), 1);
    }

    /**
     * Set the value of the field 'payload.sample.valid'
     */
    public void set_payload_sample_valid(byte value) {
        setUIntBEElement(offsetBits_payload_sample_valid(), 1, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sample.valid'
     * WARNING: This field is not an even-sized number of bytes (1 bits).
     */
    public static int size_payload_sample_valid() {
        return (1 / 8) + 1;
    }

    /**
     * Return the size, in bits, of the field 'payload.sample.valid'
     */
    public static int sizeBits_payload_sample_valid() {
        return 1;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.sample.no
    //   Field type: short
    //   Offset (bits): 57
    //   Size (bits): 15
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.sample.no' is signed (false).
     */
    public static boolean isSigned_payload_sample_no() {
        return false;
    }

    /**
     * Return whether the field 'payload.sample.no' is an array (false).
     */
    public static boolean isArray_payload_sample_no() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.sample.no'
     * WARNING: This field is not byte-aligned (bit offset 57).
     */
    public static int offset_payload_sample_no() {
        return (57 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.sample.no'
     */
    public static int offsetBits_payload_sample_no() {
        return 57;
    }

    /**
     * Return the value (as a short) of the field 'payload.sample.no'
     */
    public short get_payload_sample_no() {
        return (short)getUIntBEElement(offsetBits_payload_sample_no(), 15);
    }

    /**
     * Set the value of the field 'payload.sample.no'
     */
    public void set_payload_sample_no(short value) {
        setUIntBEElement(offsetBits_payload_sample_no(), 15, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.sample.no'
     * WARNING: This field is not an even-sized number of bytes (15 bits).
     */
    public static int size_payload_sample_no() {
        return (15 / 8) + 1;
    }

    /**
     * Return the size, in bits, of the field 'payload.sample.no'
     */
    public static int sizeBits_payload_sample_no() {
        return 15;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.t1
    //   Field type: short
    //   Offset (bits): 72
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.t1' is signed (false).
     */
    public static boolean isSigned_payload_t1() {
        return false;
    }

    /**
     * Return whether the field 'payload.t1' is an array (false).
     */
    public static boolean isArray_payload_t1() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.t1'
     */
    public static int offset_payload_t1() {
        return (72 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.t1'
     */
    public static int offsetBits_payload_t1() {
        return 72;
    }

    /**
     * Return the value (as a short) of the field 'payload.t1'
     */
    public short get_payload_t1() {
        return (short)getSIntBEElement(offsetBits_payload_t1(), 16);
    }

    /**
     * Set the value of the field 'payload.t1'
     */
    public void set_payload_t1(short value) {
        setSIntBEElement(offsetBits_payload_t1(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.t1'
     */
    public static int size_payload_t1() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.t1'
     */
    public static int sizeBits_payload_t1() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.t2
    //   Field type: short
    //   Offset (bits): 88
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.t2' is signed (false).
     */
    public static boolean isSigned_payload_t2() {
        return false;
    }

    /**
     * Return whether the field 'payload.t2' is an array (false).
     */
    public static boolean isArray_payload_t2() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.t2'
     */
    public static int offset_payload_t2() {
        return (88 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.t2'
     */
    public static int offsetBits_payload_t2() {
        return 88;
    }

    /**
     * Return the value (as a short) of the field 'payload.t2'
     */
    public short get_payload_t2() {
        return (short)getSIntBEElement(offsetBits_payload_t2(), 16);
    }

    /**
     * Set the value of the field 'payload.t2'
     */
    public void set_payload_t2(short value) {
        setSIntBEElement(offsetBits_payload_t2(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.t2'
     */
    public static int size_payload_t2() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.t2'
     */
    public static int sizeBits_payload_t2() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.t3
    //   Field type: short
    //   Offset (bits): 104
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.t3' is signed (false).
     */
    public static boolean isSigned_payload_t3() {
        return false;
    }

    /**
     * Return whether the field 'payload.t3' is an array (false).
     */
    public static boolean isArray_payload_t3() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.t3'
     */
    public static int offset_payload_t3() {
        return (104 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.t3'
     */
    public static int offsetBits_payload_t3() {
        return 104;
    }

    /**
     * Return the value (as a short) of the field 'payload.t3'
     */
    public short get_payload_t3() {
        return (short)getSIntBEElement(offsetBits_payload_t3(), 16);
    }

    /**
     * Set the value of the field 'payload.t3'
     */
    public void set_payload_t3(short value) {
        setSIntBEElement(offsetBits_payload_t3(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.t3'
     */
    public static int size_payload_t3() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.t3'
     */
    public static int sizeBits_payload_t3() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.t4
    //   Field type: short
    //   Offset (bits): 120
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.t4' is signed (false).
     */
    public static boolean isSigned_payload_t4() {
        return false;
    }

    /**
     * Return whether the field 'payload.t4' is an array (false).
     */
    public static boolean isArray_payload_t4() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.t4'
     */
    public static int offset_payload_t4() {
        return (120 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.t4'
     */
    public static int offsetBits_payload_t4() {
        return 120;
    }

    /**
     * Return the value (as a short) of the field 'payload.t4'
     */
    public short get_payload_t4() {
        return (short)getSIntBEElement(offsetBits_payload_t4(), 16);
    }

    /**
     * Set the value of the field 'payload.t4'
     */
    public void set_payload_t4(short value) {
        setSIntBEElement(offsetBits_payload_t4(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.t4'
     */
    public static int size_payload_t4() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.t4'
     */
    public static int sizeBits_payload_t4() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.t5
    //   Field type: short
    //   Offset (bits): 136
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.t5' is signed (false).
     */
    public static boolean isSigned_payload_t5() {
        return false;
    }

    /**
     * Return whether the field 'payload.t5' is an array (false).
     */
    public static boolean isArray_payload_t5() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.t5'
     */
    public static int offset_payload_t5() {
        return (136 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.t5'
     */
    public static int offsetBits_payload_t5() {
        return 136;
    }

    /**
     * Return the value (as a short) of the field 'payload.t5'
     */
    public short get_payload_t5() {
        return (short)getSIntBEElement(offsetBits_payload_t5(), 16);
    }

    /**
     * Set the value of the field 'payload.t5'
     */
    public void set_payload_t5(short value) {
        setSIntBEElement(offsetBits_payload_t5(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.t5'
     */
    public static int size_payload_t5() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.t5'
     */
    public static int sizeBits_payload_t5() {
        return 16;
    }

    /////////////////////////////////////////////////////////
    // Accessor methods for field: payload.t6
    //   Field type: short
    //   Offset (bits): 152
    //   Size (bits): 16
    /////////////////////////////////////////////////////////

    /**
     * Return whether the field 'payload.t6' is signed (false).
     */
    public static boolean isSigned_payload_t6() {
        return false;
    }

    /**
     * Return whether the field 'payload.t6' is an array (false).
     */
    public static boolean isArray_payload_t6() {
        return false;
    }

    /**
     * Return the offset (in bytes) of the field 'payload.t6'
     */
    public static int offset_payload_t6() {
        return (152 / 8);
    }

    /**
     * Return the offset (in bits) of the field 'payload.t6'
     */
    public static int offsetBits_payload_t6() {
        return 152;
    }

    /**
     * Return the value (as a short) of the field 'payload.t6'
     */
    public short get_payload_t6() {
        return (short)getSIntBEElement(offsetBits_payload_t6(), 16);
    }

    /**
     * Set the value of the field 'payload.t6'
     */
    public void set_payload_t6(short value) {
        setSIntBEElement(offsetBits_payload_t6(), 16, value);
    }

    /**
     * Return the size, in bytes, of the field 'payload.t6'
     */
    public static int size_payload_t6() {
        return (16 / 8);
    }

    /**
     * Return the size, in bits, of the field 'payload.t6'
     */
    public static int sizeBits_payload_t6() {
        return 16;
    }

}