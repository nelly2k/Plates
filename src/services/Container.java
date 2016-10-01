package services;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Container {

    public static <T> T Resolve(Class<T> type)  {
        Constructor[] allConstructors = type.getDeclaredConstructors();
        Constructor ctor = allConstructors[0];
        Class<?>[] pType  = ctor.getParameterTypes();
        List<Object> args = new ArrayList<Object>();

        for (Class<?> aPType : pType) {
            Object arg = Resolve(aPType.getClass());
            args.add(arg);
        }
        try {
            return (T)ctor.newInstance(args.toArray());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }
}
