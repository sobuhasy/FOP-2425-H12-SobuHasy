package h12.io.compress.rle;

import h12.io.BitInputStream;
import h12.io.BitOutStream;
import h12.io.BufferedBitInputStream;
import h12.io.BufferedBitOutputStream;
import h12.io.compress.Decompressor;
import h12.lang.MyBit;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple decompressor that uses the running length encoding algorithm to decompress data.
 *
 * <p>E.g. the input 100010000000010010000110 would be decompressed to 111111111111111111110000111111 to represent
 * 20 ones, 4 zeros, and 6 ones.
 *
 * @author Per Göttlicher, Nhan Huynh
 */
@DoNotTouch
public class BitRunningLengthDecompressor implements Decompressor {

    /**
     * The input stream to decompress from.
     */
    @DoNotTouch
    private final BitInputStream in;

    /**
     * The output stream to write to.
     */
    @DoNotTouch
    private final BitOutStream out;

    /**
     * Creates a new decompressor with the given input to decompress and output to write to.
     *
     * @param in  the input stream to decompress from
     * @param out the output stream to write to
     */
    @DoNotTouch
    public BitRunningLengthDecompressor(InputStream in, OutputStream out) {
        this.in = in instanceof BitInputStream bitIn ? bitIn : new BufferedBitInputStream(in);
        this.out = out instanceof BitOutStream bitOut ? bitOut : new BufferedBitOutputStream(out);
    }

    /**
     * Writes the given bit count times to the output stream.
     *
     * @param count the number of bits to write
     * @param bit   the bit to write
     *
     * @throws IOException if an I/O error occurs
     */
    @StudentImplementationRequired("H12.2.2")
    protected void writeBit(int count, MyBit bit) throws IOException {
        for (int i = 0; i < count; i++){
            out.writeBit(bit);
        }
    }

    @StudentImplementationRequired("H12.2.2")
    @Override
    public void decompress() throws IOException {
        while (true){
            int bit = in.readBit();
            if (bit == -1){
                break;
            }

            int count = in.read();
            if (count == -1){
                break;
            }

            MyBit myBit = MyBit.fromInt(bit);
            writeBit(count, myBit);
        }

        out.flush();
    }

    @DoNotTouch
    @Override
    public void close() throws Exception {
        in.close();
        out.close();
    }
}
