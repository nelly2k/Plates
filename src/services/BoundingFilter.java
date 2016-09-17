package services;

import org.opencv.core.Rect;

public interface BoundingFilter{
    Boolean Filter(Rect rect);
}
