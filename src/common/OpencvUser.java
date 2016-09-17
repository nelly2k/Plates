package common;

import org.opencv.core.Core;

public class OpencvUser extends BaseClass {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
}
