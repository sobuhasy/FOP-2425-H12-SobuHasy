package h12.io;

import h12.lang.MyBit;
import h12.lang.MyByte;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.awt.dnd.InvalidDnDOperationException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream that writes bits to an underlying output stream.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public class BufferedBitOutputStream extends BitOutStream {
    /**
     * The underlying output stream.
     */
    @DoNotTouch
    private final OutputStream underlying;

    /**
     * The buffer used for writing bits.
     */
    private MyByte buffer = new MyByte();

    /**
     * The position of the next bit to write in the buffer.
     */
    private int position = MyByte.NUMBER_OF_BITS - 1;

    /**
     * Constructs a new bit output stream with the specified underlying output stream.
     *
     * @param underlying the underlying output stream
     */
    @DoNotTouch
    public BufferedBitOutputStream(OutputStream underlying) {
        this.underlying = underlying;
    }

    /**
     * Flushes the buffer if it is not empty to write the remaining bits to the underlying output stream.
     *
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.1.2")
    protected void flushBuffer() throws IOException {
        if (buffer != null && position < MyByte.NUMBER_OF_BITS - 1){
            underlying.write(buffer.intValue());
            buffer = new MyByte();
            position = MyByte.NUMBER_OF_BITS - 1;
        }
    }

    @StudentImplementationRequired("H12.1.2")
    @Override
    public void writeBit(MyBit bit) throws IOException {
        final int INVALID = -1;
        if (position == INVALID){
            throw new IOException("Stream is closed");
        }

        buffer.set(position, bit);
        position--;

        if (position < 0){
            flushBuffer();
        }
    }

    @StudentImplementationRequired("H12.1.2")
    @Override
    public void write(int b) throws IOException {
        if (b < MyByte.MIN_VALUE || b > MyByte.MAX_VALUE){
            throw new IllegalArgumentException("Invalid byte value: " + b);
        }

        MyByte byteToWrite = new MyByte(b);
        for (int i = MyByte.NUMBER_OF_BITS - 1; i >= 0; i--){
            writeBit(byteToWrite.get(i));
        }
    }

    @DoNotTouch
    @Override
    public void flush() throws IOException {
        flushBuffer();
        underlying.flush();
    }

    @DoNotTouch
    @Override
    public void close() throws IOException {
        flush();
        underlying.close();
    }
}
