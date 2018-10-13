public class HammingController {
    private EncodeMessage mEncodeMessage;

    HammingController() {
        mEncodeMessage = new EncodeMessage();
    }

    public String getEncodedMessage(String message) {
        ResultProcess encodeResult = mEncodeMessage.encodeMessage(message);

        return processResult(encodeResult);
    }

    public String getCorrectedMessage(int mistakePlace) {
        ResultProcess mistakeResult = mEncodeMessage.resolveMistake(mistakePlace);

        return processResult(mistakeResult);
    }

    private String processResult(ResultProcess result) {
        switch (result) {
            case RESULT_SUCCESS:
                System.out.println("Process success");
                return mEncodeMessage.getHammingMessage();
            case RESULT_EMPTY_STR:
                System.out.println("Empty string entered!");
                return  null;
            case RESULT_INCORRECT_PLACE:
                System.out.println("Incorrect mistake place!");
                return null;
            case RESULT_CONVERT_ERROR:
                System.out.println("Error in getting bytes!");
                return null;
        }

        System.out.println("Unrecognized error!");
        return null;
    }
}

