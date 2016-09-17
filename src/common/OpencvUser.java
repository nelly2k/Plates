package common;

import org.opencv.core.Core;

/**
 * Created by Nelli on 17/09/2016.
 */
public class OpencvUser {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
}
