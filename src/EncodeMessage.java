import java.math.BigInteger;
import java.util.Arrays;

public class EncodeMessage {
    private final static int MESSAGE_RADIX = 2;

    private int mParityCount;
    private int[] mControlBitsMessage;

    /**
     * Run method that encode message by Hamming algorithm
     * @param message
     * @return
     */
    public ResultProcess encodeMessage(String message) {
        if(message.equals("")) {
            return ResultProcess.RESULT_EMPTY_STR;
        }

        System.out.println("String to Encoding: " + message);

        String binaryString = null;
        try {
            binaryString = new BigInteger(message.getBytes("UTF-8")).toString(MESSAGE_RADIX);
        } catch (Exception exception) {
            exception.getLocalizedMessage();
            return ResultProcess.RESULT_CONVERT_ERROR;
        }

        mParityCount = getParityBits(binaryString);
        int controlMessageLength = binaryString.length() + mParityCount;
        mControlBitsMessage = new int[controlMessageLength + 1];

        addParityBits(binaryString);

        System.out.print("Final Hamming Encoded Message: \n");
        for (int i = 1; i <= mControlBitsMessage.length - 1; i++) {
            System.out.print(mControlBitsMessage[i]);
        }

        return ResultProcess.RESULT_SUCCESS;
    }

    /**
     * Function that resolve mistake at message.
     * @param errorPlace
     * @return
     */
    public ResultProcess resolveMistake(int errorPlace) {

        int[] correctMessage = Arrays.copyOfRange(this.mControlBitsMessage, 0, this.mControlBitsMessage.length);

        if (errorPlace != 0) {
            if (mControlBitsMessage[errorPlace] == 0) {
                mControlBitsMessage[errorPlace] = 1;
            } else
                mControlBitsMessage[errorPlace] = 0;
        } else {
            return ResultProcess.RESULT_INCORRECT_PLACE;
        }

        System.out.println("Message with the error: ");
        for (int i = 1; i <= mControlBitsMessage.length-1; i++) {
            System.out.print(mControlBitsMessage[i]);
        }

        checkControlBits();

        System.out.print("\nHamming new Encoded Message : \n");
        for (int i = 1; i <= mControlBitsMessage.length-1; i++) {
            System.out.print(mControlBitsMessage[i]);
        }
        System.out.println();

        int sum = 0;
        for (int i = 0; i < mControlBitsMessage.length -1; i++)
            if (i == errorPlace) {
                i++;
            } else if (mControlBitsMessage[i] != correctMessage[i]) {
                System.out.println("Different parity bits on position: " + i);
                sum += i;
            }
        System.out.println("Incorrect bits on position: " + sum);

        if (mControlBitsMessage[sum] == 0) {
            mControlBitsMessage[sum] = 1;
        } else
            mControlBitsMessage[sum] = 0;
        checkControlBits();

        return ResultProcess.RESULT_SUCCESS;
    }

    /**
     *
     * @return
     */
    public String getHammingMessage() {
        return Arrays.toString(mControlBitsMessage);
    }

    /**
     * Calculate and return parity bits.
     * @param binaryString
     * @return
     */
    private int getParityBits(String binaryString) {

        int binaryMessageSize = binaryString.length();
        System.out.println("Lenght of binary code: " + binaryMessageSize);
        int parityCount = 0;

        while (!(binaryMessageSize + parityCount + 1 <= Math.pow(2, parityCount))) {
            parityCount++;
        }

        System.out.println("Number of parity bits needed: " + parityCount);
        return parityCount;
    }

    /**
     * Add parity bits into message.
     * @param binaryMessage
     */
    private void addParityBits(String binaryMessage) {

        int powerOfTwoValue = 0;

        int messageOffset = 0;
        int powerOffset = 0;

        for (int i = 1; i <= mControlBitsMessage.length - 1; i++) {
            powerOfTwoValue = (int) Math.pow(2, powerOffset);
            if (i % powerOfTwoValue != 0) {
                mControlBitsMessage[i] = Integer.parseInt(Character.toString(binaryMessage.charAt(messageOffset)));
                messageOffset++;
            } else {
                powerOffset++;
            }
        }

        checkControlBits();
    }

    /**
     * Check control bits in Hamming algorithm.
     */
    private void checkControlBits() {

        for (int i = 0; i < mParityCount; i++) {
            int initStep = (int) Math.pow(2, i);
            int nextStep = initStep * 2;
            int dynamicStep = initStep;
            int permanentStep = dynamicStep;

            while (true) {
                for (int k = dynamicStep; k <= dynamicStep + initStep - 1; k++) {
                    permanentStep = k;
                    if (k > mControlBitsMessage.length - 1) {
                        break;
                    }
                    mControlBitsMessage[initStep] ^= mControlBitsMessage[permanentStep];
                }
                if (permanentStep > mControlBitsMessage.length - 1) {
                    break;
                } else {
                    dynamicStep = dynamicStep + nextStep;
                }
            }
        }
    }
}
