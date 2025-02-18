package h12.io;

import h12.lang.MyByte;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that reads bits from an underlying input stream.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public class BufferedBitInputStream extends BitInputStream {

    /**
     * The value returned when the stream has reached the end or the beginning of the stream.
     */
    private static final int INVALID = -1;

    /**
     * The underlying input stream.
     */
    @DoNotTouch
    private final InputStream underlying;

    /**
     * The buffer used for reading bits.
     */
    private @Nullable MyByte buffer;

    /**
     * The position of the next bit to read in the buffer.
     */
    private int position = INVALID;

    /**
     * Constructs a new bit input stream with the specified underlying input stream.
     *
     * @param underlying the underlying input stream
     */
    @DoNotTouch
    public BufferedBitInputStream(InputStream underlying) {
        this.underlying = underlying;
    }

    /**
     * Fetches the next byte from the underlying input stream to the buffer.
     *
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.1.1")
    protected void fetch() throws IOException {
        int nextByte = underlying.read();
        if (nextByte == -1){
            buffer = null;
            position = INVALID;
        } else {
            buffer = new MyByte(nextByte);
            position = MyByte.MAX_POSITION;
        }
    }

    @StudentImplementationRequired("H12.1.1")
    @Override
    public int readBit() throws IOException {
        if (buffer == null || position == INVALID){
            fetch();
            if(buffer == null){
                return INVALID;
            }
        }

        int bit = buffer.get(position).intValue();
        position--;

        if (position < MyByte.MIN_POSITION){
            fetch();
        }

        return bit;
    }

    @StudentImplementationRequired("H12.1.1")
    @Override
    public int read() throws IOException {
        if (buffer == null && position == INVALID){
            fetch();
            if (buffer == null){
                return INVALID;
            }
        }

        int byteValue = 0;
        int bitsRead = 0;

        for (int i = 0; i < MyByte.NUMBER_OF_BITS; i++){
            int bit = readBit();
            if (bit == INVALID){
                break;
            }
            byteValue = (byteValue << 1) | bit;
            bitsRead++;
        }

        if (bitsRead == 0){
            return INVALID;
        }

        while (bitsRead < MyByte.NUMBER_OF_BITS){
            byteValue = (byteValue << 1);
            bitsRead++;
        }

        return byteValue;
    }

    /**
     * Reads up to {@code len} bytes of data from the input stream into an array of bytes.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array {@code b}
     *            at which the data is written.
     * @param len the maximum number of bytes to read.
     *
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the
     *     stream has been reached.
     * @throws IOException if an I/O error occurs.
     */
    @DoNotTouch
    public int read(byte @NotNull [] b, int off, int len) throws IOException {
        int read = 0;
        for (int i = 0; i < len; i++) {
            int value = read();
            // In case we reached the end of the stream, and the buffer is empty, return -1
            if (value == INVALID && i == 0) {
                return -1;
            }
            // In case we reached the end of the stream, return the number of values read so far
            if (value == INVALID) {
                return read;
            }
            b[off + i] = (byte) value;
            read++;
        }
        return read;
    }

    @DoNotTouch
    @Override
    public void close() throws IOException {
        underlying.close();
    }
}
